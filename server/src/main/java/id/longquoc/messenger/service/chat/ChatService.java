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
import id.longquoc.messenger.mapper.MessageMapper;
import id.longquoc.messenger.model.Conversation;
import id.longquoc.messenger.model.Message;
import id.longquoc.messenger.model.Notification;
import id.longquoc.messenger.model.User;
import id.longquoc.messenger.repository.ConversationRepository;
import id.longquoc.messenger.repository.MessageRepository;
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
    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
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
            Message messageToSave = Message.builder()
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

    @SneakyThrows
    @Transactional
    @Override
    public void processMessage(NotificationDTO notificationDTO, Principal principal){
        Notification notification = new Notification();
        notification.setTime(Instant.now());
        notification.setType(notificationDTO.getType());
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> metadata = new HashMap<>();
        String loggedUser = principal.getName();
        try {
            metadata = mapper.readValue(notificationDTO.getMetadata(), Map.class);
            notification.addToMetadata("CONVERSATION", metadata.get("CONVERSATION"));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        switch (notificationDTO.getType()) {
            case INCOMING_CALL:
                notification.setContent(principal.getName() + " is calling you!");
                sendToUser(loggedUser, notification, false);
                break;
            case ACCEPTED_CALL:
                notification.setContent(principal.getName() + " accepted your call!");
                sendToUser(loggedUser, notification, false);
                break;
            case CANCELLED_CALL:
                notification.setContent(principal.getName() + " cancelled the call!");
                sendToUser(loggedUser, notification, false);
                break;
            case REJECTED_CALL:
                notification.setContent(principal.getName() + " rejected your call!");
                sendToUser(loggedUser, notification, false);
                break;
            case INCOMING_MESSAGE:
                notification.setContent("New message from " + loggedUser);
                String message = metadata.get("MESSAGE");
                notification.addToMetadata("MESSAGE", message);
                notification.addToMetadata("USER", loggedUser);
                sendToUser(loggedUser, notification, true);
                break;
        }
    }
    private void sendToUser(String sender, Notification notification, boolean saveMessage) {
        Map<String, String > metadata = notification.getMetadata();
        UUID conversationId = UUID.fromString(metadata.get("CONVERSATION"));
        Set<User> userDTOS = getUsersInConversation(conversationId);
        userDTOS.stream()
                .map(User::getUsername)
                .filter(s -> !s.equals(sender))
                .forEach(username -> {
                    notification.addToMetadata("USER", new Gson().toJson(
                            new UserChatDto(null, sender)
                    ));
                    simpMessagingTemplate.convertAndSendToUser(username, "/queue/messages", notification);
                    if (saveMessage) {
                        String message = metadata.get("MESSAGE");
                        storeMessage(message, conversationId, username);
                    }
                });
    }
    private Set<User> getUsersInConversation(UUID conversationId) {
        Conversation conversation = conversationService.getConversationById(conversationId);
        if (conversation != null) {
            return new HashSet<>(conversation.getParticipants());
        }
        return new HashSet<>();
    }

    @SneakyThrows
    private void storeMessage(String content, UUID conversationId, String sender){
        Conversation conversation = conversationService.getConversationById(conversationId);

        Message message = new Message();
        message.setSender(userRepository.findByUsername(sender));
        message.setContent(content);
        message.setConversation(conversation);
        message.setCreatedAt(Instant.now());
        message.setDateSent(Instant.now());
        conversationService.saveMessageToList(message, conversationId);
    }
}
