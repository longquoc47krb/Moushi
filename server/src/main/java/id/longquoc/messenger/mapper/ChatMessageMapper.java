package id.longquoc.messenger.mapper;

import id.longquoc.messenger.model.Conversation;
import id.longquoc.messenger.model.User;
import id.longquoc.messenger.payload.request.ConversationRequest;
import id.longquoc.messenger.payload.response.ConversationResponse;
import id.longquoc.messenger.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.function.Function;
@Component

@RequiredArgsConstructor
public class ConversationMapper implements Function<Conversation, ConversationResponse> {
    private final UserService userService;
    private final UserMapper userMapper;
    @Override
    public ConversationResponse apply(Conversation conversation) {
        var participants = conversation.getParticipants().stream().map(User::getId).toList();
        return ConversationResponse.builder()
                .id(conversation.getId())
                .participants(userService.findUsersById(participants))
                .dateStarted(conversation.getUpdateDate())
                .groupConversation(conversation.isGroupConversation())
                .build();
    }
    public List<ConversationResponse> mapConversations(List<Conversation> conversations) {
        return conversations.stream().map(this).toList();
    }
    public Conversation mapConversationRequest(ConversationRequest conversationRequest) {
        var users = userService.findUsersById(conversationRequest.getParticipants());
        return Conversation.builder()
                .updateDate(Instant.now())
                .participants(users)
                .build();
    }
}
