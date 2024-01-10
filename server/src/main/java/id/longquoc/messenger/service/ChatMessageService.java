package id.longquoc.messenger.service;

import id.longquoc.messenger.enums.MessageState;
import id.longquoc.messenger.mapper.ConversationMapper;
import id.longquoc.messenger.model.ChatMessage;
import id.longquoc.messenger.model.Conversation;
import id.longquoc.messenger.payload.request.MessageReq;
import id.longquoc.messenger.payload.response.ConversationResponse;
import id.longquoc.messenger.repository.ChatMessageRepository;
import id.longquoc.messenger.service.interfaces.IChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatMessageService implements IChatMessageService {
    private final ConversationService conversationService;
    private final UserService userService;
    private final ChatMessageRepository messageRepository;
    private final ConversationMapper conversationMapper;
    @Override
    public ChatMessage createMessage(MessageReq messageReq) {
        ConversationResponse conversation = conversationService.getConversationById(messageReq.getConversationId());
        ChatMessage chatMessage = ChatMessage.builder()
                .createdAt(Instant.now())
                .content(messageReq.getContent())
                .conversation(conversationMapper.mapConversationResponse(conversation))
                .image(messageReq.getImage())
                .dateSent(Instant.now())
                .states(List.of(MessageState.DELIVERED))
                .sender(userService.findUserByUsername(messageReq.getUsername()))
                .build();
        return messageRepository.save(chatMessage);
    }

    @Override
    public void removeMessageById(UUID id) {
        messageRepository.deleteById(id);
    }
}
