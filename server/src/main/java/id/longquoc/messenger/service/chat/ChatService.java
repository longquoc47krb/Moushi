package id.longquoc.messenger.service.chat;

import id.longquoc.messenger.dto.chat.ConversationDto;
import id.longquoc.messenger.dto.chat.MessageDto;
import id.longquoc.messenger.dto.chat.PrevMessageDto;
import id.longquoc.messenger.dto.user.OnlineUserDto;
import id.longquoc.messenger.dto.user.UserStateDto;
import id.longquoc.messenger.enums.MessageState;
import id.longquoc.messenger.mapper.MessageMapper;
import id.longquoc.messenger.mapper.UserMapper;
import id.longquoc.messenger.model.Conversation;
import id.longquoc.messenger.model.Message;
import id.longquoc.messenger.model.User;
import id.longquoc.messenger.repository.ConversationRepository;
import id.longquoc.messenger.repository.MessageRepository;
import id.longquoc.messenger.repository.UserRepository;
import id.longquoc.messenger.service.ConversationService;
import id.longquoc.messenger.service.UserService;
import id.longquoc.messenger.service.interfaces.IChatService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class ChatService implements IChatService {
    private static final int MAX_PAGE_SIZE = 3;
    private final ConversationRepository conversationRepository;
    private final ConversationService conversationService;
    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageSender devMessageSender;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final UserService userService;
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
    public void submitMessage(MessageDto messageDto, StompHeaderAccessor accessor){
        List<User> users = messageDto.getStates() == null || messageDto.getStates().isEmpty()
                ? userService.findUsersInConversation(UUID.fromString(messageDto.getConversationId())) : null;
        MessageDto savedMessageToSend = saveOrUpdateMessage(users, messageDto);
        assert users != null;
        boolean conversationExists = conversationService.participantsHasConversation(userMapper.toListUUID(users));
        if (!conversationExists) createAndSendConversation(Objects.requireNonNull(users), accessor);
        Conversation conversation = conversationService.getConversationById(UUID.fromString(messageDto.getConversationId()));

        simpMessagingTemplate.convertAndSend("/user/queue/conversations/" + conversation.getId(),
                "[" + getTimestamp() + "]:" + messageDto.getSender() + ":" + messageDto.getContent()
        );

    }
    private User[] fetchUsersByUsername(String username){
        User user = userRepository.findByEmailOrUsername(username, username);
        return null;
    }
}
