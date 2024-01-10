package id.longquoc.messenger.config.websocket;

import com.google.gson.Gson;
import id.longquoc.messenger.dto.user.UserStateDto;
import id.longquoc.messenger.enums.NotificationType;
import id.longquoc.messenger.enums.UserState;
import id.longquoc.messenger.model.Notification;
import id.longquoc.messenger.model.User;
import id.longquoc.messenger.service.UserService;
import id.longquoc.messenger.service.chat.UserSocketService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.security.Principal;
import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);
    private final SimpMessagingTemplate template;
    private final UserSocketService userSocketService;
    private final UserService userService;
    private final DestinationStore destinationStore;
    private void handleSession(String username, String logText, UserState userState) {
        logger.info(logText);
        Notification notification = new Notification();
        notification.setTime(Instant.now());
        notification.setContent(logText);
        notification.setType(NotificationType.USER_STATE);
        notification.addToMetadata("USER", username);
        notification.addToMetadata("STATE", userState.name());
        // Notify everyone about user.
        template.convertAndSend("/topic/public", notification);
    }

    private void onUserSubscribe(String username) {
        Notification notification = new Notification();
        notification.setTime(Instant.now());
        notification.setType(NotificationType.ONLINE_USERS);
        List<UserStateDto> userSocketDTOS = userSocketService.getSubscribedSocketUsersByDestination("/topic/public");
        userSocketDTOS.removeIf(userState -> userState.getUsername().equals(username));
        notification.addToMetadata("USERS", String.join(", ", new Gson().toJson(userSocketDTOS)));
        template.convertAndSendToUser(username
                , "/queue/messages", notification);
    }
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        Principal principal = event.getUser();
        assert principal != null;
        if (principal != null) {
            String username = principal.getName();
            userService.updateUserStateAndLastLogin(username, UserState.ONLINE, Instant.now());
            destinationStore.connectWebsocket(username);
        }
    }
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        Principal principal = event.getUser();
        if (principal != null) {
            String username = principal.getName();
            destinationStore.disconnectWebsocket(username);
            userService.updateUserStateAndLastLogin(username, UserState.OFFLINE, Instant.now());
            userSocketService.removeByUsername(username);
        }
    }
    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
        Principal principal = event.getUser();
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        if (principal != null) {
            String username = principal.getName();
            GenericMessage message = (GenericMessage) event.getMessage();
            String simpDestination = (String) message.getHeaders().get("simpDestination");
            String destination = "/user/" + username + "/queue/messages";
            destinationStore.registerDestination(sha.getSessionId(), sha.getDestination(), username);
            if (simpDestination.startsWith(destination)) {
                onUserSubscribe(principal.getName());
            }
        }
    }
}
