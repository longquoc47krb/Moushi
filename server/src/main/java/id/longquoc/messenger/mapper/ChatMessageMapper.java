package id.longquoc.messenger.mapper;

import id.longquoc.messenger.model.ChatMessage;
import id.longquoc.messenger.model.Conversation;
import id.longquoc.messenger.model.User;
import id.longquoc.messenger.payload.request.ConversationRequest;
import id.longquoc.messenger.payload.response.ConversationResponse;
import id.longquoc.messenger.payload.response.MessageResponse;
import id.longquoc.messenger.payload.response.UserResponseSecure;
import id.longquoc.messenger.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.function.Function;

@Component

@RequiredArgsConstructor
public class ChatMessageMapper implements Function<ChatMessage, MessageResponse> {
    private final UserService userService;
    private final UserMapper userMapper;
    @Override
    public MessageResponse apply(ChatMessage chatMessage) {
        return MessageResponse.builder()
                .id(chatMessage.getId())
                .content(chatMessage.getContent())
                .image(chatMessage.getImage())
                .conversationId(String.valueOf(chatMessage.getConversation().getId()))
                .sender(userMapper.toUserResponseSecure(chatMessage.getSender()))
                .dateDelivered(chatMessage.getDateDelivered())
                .dateRead(chatMessage.getDateRead())
                .dateSent(chatMessage.getDateSent())
                .states(chatMessage.getStates())
                .build();
    }
    public List<MessageResponse> mapMessageResponse(List<ChatMessage> chatMessages) {
        return chatMessages.stream().map(this).toList();
    }



}
