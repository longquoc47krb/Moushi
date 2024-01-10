package id.longquoc.messenger.service.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import id.longquoc.messenger.dto.NotificationDTO;
import id.longquoc.messenger.dto.chat.ConversationDto;
import id.longquoc.messenger.dto.chat.MessageDto;
import id.longquoc.messenger.dto.chat.PrevMessageDto;
import id.longquoc.messenger.dto.user.OnlineUserDto;
import id.longquoc.messenger.dto.user.UserChatDto;
import id.longquoc.messenger.dto.user.UserStateDto;
import id.longquoc.messenger.mapper.ChatMessageMapper;
import id.longquoc.messenger.mapper.ConversationMapper;
import id.longquoc.messenger.mapper.MessageMapper;
import id.longquoc.messenger.model.Conversation;
import id.longquoc.messenger.model.ChatMessage;
import id.longquoc.messenger.model.Notification;
import id.longquoc.messenger.model.User;
import id.longquoc.messenger.payload.request.MessageReq;
import id.longquoc.messenger.payload.response.ConversationResponse;
import id.longquoc.messenger.repository.ConversationRepository;
import id.longquoc.messenger.repository.ChatMessageRepository;
import id.longquoc.messenger.repository.UserRepository;
import id.longquoc.messenger.service.ConversationService;
import id.longquoc.messenger.service.interfaces.IChatService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.Instant;
import java.util.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class ChatService implements IChatService {
    private final ConversationRepository conversationRepository;
    private final ConversationService conversationService;
    private final ChatMessageRepository messageRepository;
    private final ConversationMapper conversationMapper;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final UserSocketService userSocketService;
    private final UserRepository userRepository;
    @Override
    public List<OnlineUserDto> fetchOnlineUsers() {

        List<User> onlineUsers = userRepository.getOnlineUsersExceptThis();

        return onlineUsers
                .stream()
                .map(MessageMapper.INSTANCE::toDto)
                .toList();
    }

    @Override
    public List<ConversationDto> fetchConversations(String username) {
        List<Conversation> conversations = conversationRepository.findAllByUsername(username);

        return conversations
                .stream()
                .map(c -> MessageMapper.INSTANCE.toConversationDto(c, username))
                .toList();
    }

    @Override
    public void createAndSendConversation(List<User> users, StompHeaderAccessor accessor) {
        Conversation newConversation = conversationRepository.save(new Conversation(users));
        Principal senderPrincipal = Objects.requireNonNull(accessor.getUser());
        String senderUsername = Objects.requireNonNull(senderPrincipal.getName());
        simpMessagingTemplate.convertAndSend("/topic/conversation." + newConversation.getId(),
                "[" + getTimestamp() + "]:" + senderUsername + ":" + accessor.getMessage()
                );


    }
    private String getTimestamp() {
        Instant date = Instant.now();
        return date.toString();
    }

    @Override
    public void changeUserState(UserStateDto userStateDto, String username) {

    }

    @Override
    public MessageDto saveOrUpdateMessage(List<User> users, MessageDto message) {
        if(message.getStates() == null || message.getStates().isEmpty()){
            ChatMessage messageToSave = ChatMessage.builder()
                    .id(message.getId())
                    .content(message.getContent())
                    .conversation(conversationRepository.findById(UUID.fromString(message.getConversationId())).get())
                    .dateSent(Instant.now())
                    .states(null)
                    .build();
            messageRepository.save(messageToSave);
            return MessageMapper.INSTANCE.toDto(messageToSave);
        }
        return null;
    }

    @Override
    public PrevMessageDto fetchPreviousChat(UUID conversationId, int page, String currentUser) {
        return null;
    }

//    @SneakyThrows
//    @Transactional
//    @Override
//    public void processMessage(NotificationDTO notificationDTO, Principal principal){
//        Notification notification = new Notification();
//        notification.setTime(Instant.now());
//        notification.setType(notificationDTO.getType());
//        ObjectMapper mapper = new ObjectMapper();
//        Map<String, String> metadata = new HashMap<>();
//        String loggedUser = principal.getName();
//        try {
//            metadata = mapper.readValue(notificationDTO.getMetadata(), Map.class);
//            notification.addToMetadata("CONVERSATION", metadata.get("CONVERSATION"));
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        switch (notificationDTO.getType()) {
//            case INCOMING_CALL:
//                notification.setContent(principal.getName() + " is calling you!");
//                sendToUser(loggedUser, notification, false);
//                break;
//            case ACCEPTED_CALL:
//                notification.setContent(principal.getName() + " accepted your call!");
//                sendToUser(loggedUser, notification, false);
//                break;
//            case CANCELLED_CALL:
//                notification.setContent(principal.getName() + " cancelled the call!");
//                sendToUser(loggedUser, notification, false);
//                break;
//            case REJECTED_CALL:
//                notification.setContent(principal.getName() + " rejected your call!");
//                sendToUser(loggedUser, notification, false);
//                   break;
//            case INCOMING_MESSAGE:
//                notification.setContent("New message from " + loggedUser);
//                String message = metadata.get("MESSAGE");
//                notification.addToMetadata("MESSAGE", message);
//                notification.addToMetadata("USER", loggedUser);
//                sendToUser(loggedUser, notification, true);
//                break;
//        }
//    }
    @Transactional
    public void processPrivateMessage(MessageReq messageReq){
        conversationService.sendMessage(messageReq.getConversationId(),messageReq);
        String usernameSender = messageReq.getUsername();
        sendToUser(usernameSender, messageReq);
    }
    private void sendToUser(String sender, MessageReq messageReq) {
        UUID conversationId = messageReq.getConversationId();
        Set<User> userDTOS = getUsersInConversation(conversationId);
        userDTOS.stream()
                .map(User::getUsername)
                .filter(s -> !s.equals(sender))
                .forEach(username -> {
                    simpMessagingTemplate.convertAndSendToUser(username, "/private-messages", messageReq);
                });
    }
//    private void sendToUser(String sender, Notification notification, boolean saveMessage) {
//        Map<String, String > metadata = notification.getMetadata();
//        UUID conversationId = UUID.fromString(metadata.get("CONVERSATION"));
//        Set<User> userDTOS = getUsersInConversation(conversationId);
//        userDTOS.stream()
//                .map(User::getUsername)
//                .filter(s -> !s.equals(sender))
//                .forEach(username -> {
//                    notification.addToMetadata("USER", new Gson().toJson(
//                            new UserChatDto(null, sender)
//                    ));
//                    simpMessagingTemplate.convertAndSendToUser(username, "/queue/messages", notification);
//                    if (saveMessage) {
//                        String message = metadata.get("MESSAGE");
//                        storeMessage(new MessageReq(username, conversationId, message, ""));
//                    }
//                });
//    }
    private void sendToSubscribers(String subscribersDestination, String destination, String loggedUser, Object payload) {
        userSocketService.getSubscribedSocketUsersByDestination(subscribersDestination)
                .stream()
                .map(UserStateDto::getUsername)
                .filter(name -> !loggedUser.equals(name))
                .forEach(subscriber -> simpMessagingTemplate.convertAndSendToUser(subscriber, destination, payload));
    }
    private Set<User> getUsersInConversation(UUID conversationId) {
        ConversationResponse conversation = conversationService.getConversationById(conversationId);
        if (conversation != null) {
            return new HashSet<>(conversation.getParticipants());
        }
        return new HashSet<>();
    }

    @SneakyThrows
    public void storeMessage(MessageReq messageReq){
        try {
            ConversationResponse conversation = conversationService.getConversationById(messageReq.getConversationId());

            ChatMessage message = ChatMessage.builder()
                    .sender(userRepository.findByUsername(messageReq.getUsername()))
                    .content(messageReq.getContent())
                    .conversation(conversationMapper.mapConversationResponse(conversation))
                    .createdAt(Instant.now())
                    .dateSent(Instant.now())
                    .build();
            messageRepository.save(message);
            conversationService.sendMessage(messageReq.getConversationId(),messageReq);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
