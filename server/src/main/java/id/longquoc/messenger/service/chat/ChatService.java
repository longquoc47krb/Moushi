package id.longquoc.messenger.service.chat;

import id.longquoc.messenger.dto.chat.ConversationDto;
import id.longquoc.messenger.dto.chat.MessageDto;
import id.longquoc.messenger.dto.chat.PrevMessageDto;
import id.longquoc.messenger.dto.user.OnlineUserDto;
import id.longquoc.messenger.dto.user.UserStateDto;
import id.longquoc.messenger.mapper.MessageMapper;
import id.longquoc.messenger.model.Conversation;
import id.longquoc.messenger.model.User;
import id.longquoc.messenger.repository.ConversationRepository;
import id.longquoc.messenger.repository.MessageRepository;
import id.longquoc.messenger.repository.UserRepository;
import id.longquoc.messenger.service.interfaces.IChatService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class ChatService implements IChatService {
    private static final int MAX_PAGE_SIZE = 3;
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageSender devMessageSender;
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
    public void createAndSendConversation(User[] users, StompHeaderAccessor accessor) {

    }

    @Override
    public void changeUserState(UserStateDto userStateDto, String username) {

    }

    @Override
    public MessageDto saveOrUpdateMessage(User[] users, MessageDto message) {
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
//        User[] users = messageDto.getStates() == null || messageDto.getStates().isEmpty();
    }
}
