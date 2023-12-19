package id.longquoc.messenger.service;

import id.longquoc.messenger.mapper.ConversationMapper;
import id.longquoc.messenger.mapper.UserMapper;
import id.longquoc.messenger.model.Conversation;
import id.longquoc.messenger.model.User;
import id.longquoc.messenger.payload.request.ConversationRequest;
import id.longquoc.messenger.payload.response.ConversationResponse;
import id.longquoc.messenger.repository.ConversationRepository;
import id.longquoc.messenger.service.interfaces.IConversationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class ConversationService implements IConversationService {
    private final ConversationRepository conversationRepository;
    private final UserService userService;
    @Override
    public List<ConversationResponse> getAllConversations(UUID userId) {
      var conversations = conversationRepository.findAllByUserId(userId);
        if (conversations.isEmpty()) {
            return List.of();
        }
        var conversationMapper = new ConversationMapper(userService, new UserMapper());
        return conversationMapper.mapConversations(conversations);
    }

    @Override
    public ConversationResponse createConversation(ConversationRequest request) {
        UUID senderId = request.getParticipants().get(0);
        UUID receiverId = request.getParticipants().get(1);
        var conversationExists = doesConversationExist(senderId, receiverId);
        if(conversationExists){
            return null;
        }
        var conversationMapper = new ConversationMapper(userService, new UserMapper());
        var conversation = conversationMapper.mapConversationRequest(request);
        conversationRepository.save(conversation);
        return conversationMapper.apply(conversation);
    }

    @Override
    public Conversation getConversationById(UUID id) {
        return conversationRepository.findById(id).orElseThrow(() -> new ResolutionException("Conversation not found"));
    }

    @Override
    public boolean doesConversationExist(UUID sender, UUID receiver) {
        List<Conversation> allConversations = conversationRepository.findAll(); // Retrieve all conversations
        for (Conversation conversation : allConversations) {
            boolean senderFound = false;
            boolean receiverFound = false;

            for (User participant : conversation.getParticipants()) {
                if (participant.getId().equals(sender)) {
                    senderFound = true;
                }
                if (participant.getId().equals(receiver)) {
                    receiverFound = true;
                }
            }

            if (senderFound && receiverFound) {
                return true; // Conversation found with both sender and receiver
            }
        }

        return false;
    }
}
