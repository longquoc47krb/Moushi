package id.longquoc.messenger.config.websocket;

import id.longquoc.messenger.dto.user.UserStateDto;
import id.longquoc.messenger.enums.TypingState;
import id.longquoc.messenger.exception.UnauthorizedException;
import id.longquoc.messenger.mapper.MessageMapper;
import id.longquoc.messenger.model.Conversation;
import id.longquoc.messenger.repository.ConversationRepository;
import id.longquoc.messenger.repository.MessageRepository;
import id.longquoc.messenger.repository.UserRepository;
import id.longquoc.messenger.service.chat.ChatService;
import id.longquoc.messenger.service.chat.MessageSender;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.security.Principal;
import java.time.Instant;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketStateListener {
    private final SimpMessagingTemplate messagingTemplate;
    private final UserRepository userRepository;
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final ChatService chatService;
    private final MessageSender devMessageSender;

    /*
    TODO: This method handles the STOMP session connection event by checking whether the user is authenticated and setting their online status.
     If the user is authenticated, their status will be set to online. If the user is not authenticated, an error message will be sent,
     and an UnauthorizedException will be thrown.
     */
    @Transactional
    @EventListener
    public void handleState(SessionConnectedEvent sessionConnectedEvent){
        Principal principal = sessionConnectedEvent.getUser();
        String username = principal != null ? principal.getName() : null;
        if(username == null) {
            devMessageSender.sendError(StompHeaderAccessor.wrap(sessionConnectedEvent.getMessage()),
                    "User is not authenticated, could be a missing or invalid bearer");
            throw new UnauthorizedException("User is not authenticated, could be a missing or invalid bearer");
        }
        userRepository.setOnline(username, null, (byte) +1);
        //		Only send the list of online users if 1st session is created if there are more we don't care
        if(userRepository.getSessions(username) == 1){

        }

    }
    /*
    *TODO: This method handles the disconnection event of a STOMP session by updating the online status of the user.
    * If this is the user's final session, the last online time will also be updated.
    * */
    @Transactional
    @EventListener
    public void handleState(SessionDisconnectEvent sessionDisconnectEvent){
        Principal principal = sessionDisconnectEvent.getUser();
        String username = principal != null ? principal.getName() : null;
        if(username == null) return;
        //		Only send list of online users if there is only last session and it is also removed
        if(userRepository.getSessions(username) == 1) {
            userRepository.setOnline(username, Instant.now(), (byte) -1);
        } else {
            userRepository.setOnline(username, null, (byte) -1);
        }

    }
    /*
    TODO: This method handles the STOMP session subcribe event by checking the value of destination and taking action accordingly.
     If the destination is not authorized, an error message will be sent and logged.
    */
    @Transactional
    @EventListener
    public void handleState(SessionSubscribeEvent sessionSubscribeEvent){
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(sessionSubscribeEvent.getMessage());
        String destination = accessor.getDestination();
        switch (Objects.requireNonNull(destination)){
            case "/topic/online",
                    "/user/queue/error",
                    "/user/queue/chat",
                    "/user/queue/delete-message",
                    "/user/queue/conversations",
                    "/user/queue/previous-chat",
                    "/user/queue/change-user-state"
                    -> devMessageSender.sendReceipt(accessor, "Subscribed to - " + accessor.getDestination());

            default -> {
                devMessageSender.sendError(accessor, "Unauthorized Subscribable Destination: " + destination);
                log.error("Unauthorized Subscription Destination: " + destination);
            }
        }

    }
    private void propagateStateChange(String username, Instant lastOnline){
        UserStateDto userStateDto = UserStateDto.builder()
                .username(username)
                .lastOnline(lastOnline)
                .typingState(TypingState.IDLE)
                .build();
        conversationRepository
                .findAllByUsername(username)
                .stream()
                .map(conversation -> MessageMapper.INSTANCE.toUsername(conversation, username))
                        .forEach(usn -> messagingTemplate.convertAndSendToUser(usn, "/queue/change-user-state", userStateDto ));;
        messagingTemplate.convertAndSend("/topic/online", chatService.fetchOnlineUsers());

    }
}
