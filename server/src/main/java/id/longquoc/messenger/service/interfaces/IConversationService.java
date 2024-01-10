package id.longquoc.messenger.service.interfaces;

import id.longquoc.messenger.model.Conversation;
import id.longquoc.messenger.model.ChatMessage;
import id.longquoc.messenger.payload.request.ConversationRequest;
import id.longquoc.messenger.payload.request.MessageReq;
import id.longquoc.messenger.payload.response.ConversationResponse;
import id.longquoc.messenger.payload.response.MessageResponse;

import java.util.List;
import java.util.UUID;

public interface IConversationService {
    List<ConversationResponse> getAllConversations();
    List<ConversationResponse> getAllConversationsByUserId(UUID id);
    ConversationResponse createConversation(ConversationRequest request) throws Exception;
    ConversationResponse getConversationById(UUID id);
    boolean participantsHasConversation(List<UUID> participantIds);
    Conversation updateConversationById(UUID id);
    List<MessageResponse> getMessages(String conversationId);

    MessageResponse sendMessage(UUID conversationId, MessageReq message);
}
