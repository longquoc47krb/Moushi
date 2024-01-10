package id.longquoc.messenger.mapper;

import id.longquoc.messenger.model.Conversation;
import id.longquoc.messenger.model.User;
import id.longquoc.messenger.payload.request.ConversationRequest;
import id.longquoc.messenger.payload.response.ConversationResponse;
import id.longquoc.messenger.payload.response.MessageResponse;
import id.longquoc.messenger.service.ConversationService;
import id.longquoc.messenger.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

@Component

@RequiredArgsConstructor
public class ConversationMapper implements Function<Conversation, ConversationResponse> {
    private final UserService userService;
    private final UserMapper userMapper;
    private ConversationService conversationService;
    @Autowired
    public void setConversationService(@Lazy ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @Override
    public ConversationResponse apply(Conversation conversation) {
        var participants = conversation.getParticipants().stream().map(User::getId).toList();
        List<MessageResponse> messageResponses = conversationService.getMessages(String.valueOf(conversation.getId()));
        List<MessageResponse> sortedMessages = messageResponses.stream()
                .sorted(Comparator.comparing(MessageResponse::getDateSent))
                .toList();
        MessageResponse lastMessage = sortedMessages.get(sortedMessages.size() - 1);
        return ConversationResponse.builder()
                .id(conversation.getId())
                .participants(userService.findUsersById(participants))
                .dateStarted(conversation.getDateUpdate())
                .groupConversation(conversation.isGroupConversation())
                .messages(sortedMessages)
                .dateUpdate(lastMessage.getDateSent())
                .build();
    }
    public List<ConversationResponse> mapConversations(List<Conversation> conversations) {

        return conversations.stream().map(c -> {
            List<MessageResponse> sortedMessage = conversationService.getMessages(String.valueOf(c.getId())).stream()
                    .sorted(Comparator.comparing(MessageResponse::getDateSent))
                    .toList();
            MessageResponse lastMessage = sortedMessage.get(sortedMessage.size() - 1);
            return ConversationResponse.builder()
                    .id(c.getId())
                    .groupConversation(c.isGroupConversation())
                    .messages(sortedMessage)
                    .participants(c.getParticipants())
                    .dateUpdate(lastMessage.getDateSent())
                    .build();
        }).toList();
    }
    public Conversation mapConversationRequest(ConversationRequest conversationRequest) {
        var users = userService.findUsersById(conversationRequest.getParticipants());
        return Conversation.builder()
                .dateUpdate(Instant.now())
                .participants(users)
                .build();
    }
    public Conversation mapConversationResponse(ConversationResponse conversationResponse){
        return Conversation.builder()
                .groupConversation(conversationResponse.isGroupConversation())
                .id(conversationResponse.getId())
                .participants(conversationResponse.getParticipants())
                .build();
    }
}
