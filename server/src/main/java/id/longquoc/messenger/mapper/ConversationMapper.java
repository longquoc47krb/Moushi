package id.longquoc.messenger.mapper;

import id.longquoc.messenger.model.Conversation;
import id.longquoc.messenger.model.User;
import id.longquoc.messenger.payload.request.ConversationRequest;
import id.longquoc.messenger.payload.response.ConversationResponse;
import id.longquoc.messenger.service.UserService;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@RequiredArgsConstructor
public class ConversationMapper implements Function<Conversation, ConversationResponse> {
    private final UserService userService;
    private final UserMapper userMapper;
    @Override
    public ConversationResponse apply(Conversation conversation) {
        var participants = conversation.getParticipants().stream().map(User::getId).toList();
        return ConversationResponse.builder()
                .id(conversation.getId())
                .participants(userMapper.mapUsers(userService.findUsersById(participants)))
                .messages(conversation.getMessages())
                .dateStarted(conversation.getDateStarted())
                .build();
    }
    public List<ConversationResponse> mapConversations(List<Conversation> conversations) {
        return conversations.stream().map(this).toList();
    }
    public Conversation mapConversationRequest(ConversationRequest conversationRequest) {
        var users = userService.findUsersById(conversationRequest.getParticipants());
        return Conversation.builder()
                .dateStarted(Instant.now())
                .participants(users)
                .build();
    }
}
