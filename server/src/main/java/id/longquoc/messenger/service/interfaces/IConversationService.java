package id.longquoc.messenger.service.interfaces;

import id.longquoc.messenger.model.Conversation;
import id.longquoc.messenger.payload.request.ConversationRequest;
import id.longquoc.messenger.payload.response.ConversationResponse;

import java.util.List;
import java.util.UUID;

public interface IConversationService {
    List<ConversationResponse> getAllConversations(UUID id);
    ConversationResponse createConversation(ConversationRequest request) throws Exception;
    Conversation getConversationById(UUID id);
    boolean participantsHasConversation(List<UUID> participantIds);

}
