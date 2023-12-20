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
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.util.List;
import java.util.UUID;

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
        var conversationExists = participantsHasConversation(request.getParticipants());
        if(conversationExists){
            return null;
        }
        var conversationMapper = new ConversationMapper(userService, new UserMapper());
        Conversation conversation = conversationMapper.mapConversationRequest(request);
        try {
            conversationRepository.save(conversation);
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }

        return conversationMapper.apply(conversation);
    }

    @Override
    public Conversation getConversationById(UUID id) {
        return conversationRepository.findById(id).orElseThrow(() -> new ResolutionException("Conversation not found"));
    }

    @Override
    public boolean participantsHasConversation(List<UUID> participantIds)  {
        List<Conversation> allConversations = conversationRepository.findAll(); // Retrieve all conversations

        for (Conversation conversation : allConversations) {
            boolean allMembersFound = true;
            List<User> participantsInThisConversation = conversation.getParticipants();
            for(UUID participantId : participantIds){
                boolean found = false;
                for(User participant : participantsInThisConversation){
                    if(participant.getId().equals(participantId)){
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    allMembersFound = false;
                    break;
                }
            }
            if (allMembersFound) {
                return true;
            }
        }

        return false;
    }
}
