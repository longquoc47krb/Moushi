package id.longquoc.messenger.service;

import id.longquoc.messenger.enums.MessageState;
import id.longquoc.messenger.mapper.ChatMessageMapper;
import id.longquoc.messenger.mapper.ConversationMapper;
import id.longquoc.messenger.mapper.UserMapper;
import id.longquoc.messenger.model.ChatMessage;
import id.longquoc.messenger.model.Conversation;
import id.longquoc.messenger.model.User;
import id.longquoc.messenger.payload.request.ConversationRequest;
import id.longquoc.messenger.payload.request.MessageReq;
import id.longquoc.messenger.payload.response.ConversationResponse;
import id.longquoc.messenger.payload.response.MessageResponse;
import id.longquoc.messenger.repository.ChatMessageRepository;
import id.longquoc.messenger.repository.ConversationRepository;
import id.longquoc.messenger.service.interfaces.IConversationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConversationService implements IConversationService {
    private final ConversationRepository conversationRepository;
    private final UserService userService;
    private final ConversationMapper conversationMapper;
    private final ChatMessageMapper messageMapper;
    private final ChatMessageRepository chatMessageRepository;
    @Override
    public List<ConversationResponse> getAllConversations() {
        return conversationRepository.findAll().stream().map(conversationMapper).collect(Collectors.toList());
    }

    @Override
    public List<ConversationResponse> getAllConversationsByUserId(UUID userId) {
      var conversations = conversationRepository.findAllByUserId(userId);
      List<Conversation> conversations1 = new ArrayList<>();
      for(Conversation conversation : conversations){
          if(conversation.getDateUpdate() == null){
              List<MessageResponse> messageResponses = this.getMessages(String.valueOf(conversation.getId()));
              MessageResponse lastMessage = messageResponses.get(messageResponses.size() - 1);
              conversation.setDateUpdate(lastMessage.getDateSent());
              conversations1.add(conversation);
          } else {
              conversations1.add(conversation);
          }
      }
      if (conversations1.isEmpty()) {
            return List.of();
        }
        List<Conversation> sortedConversation = conversations1.stream().sorted(Comparator.comparing(Conversation::getDateUpdate)).toList();

        return conversationMapper.mapConversations(sortedConversation);
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
            System.out.println(ex.getMessage());
        }

        return conversationMapper.apply(conversation);
    }

    @Override
    public ConversationResponse getConversationById(UUID id) {
        Conversation conversation = conversationRepository.findById(id).orElseThrow(() -> new ResolutionException("Conversation not found"));
        return conversationMapper.apply(conversation);
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
    @Override
    public List<MessageResponse> getMessages(String conversationId) {
         List<ChatMessage> messages = chatMessageRepository.findMessagesByConversationId(UUID.fromString(conversationId));
         return messageMapper.mapMessageResponse(messages);
    }
    @Override
    @Transactional

    public MessageResponse sendMessage(UUID conversationId, MessageReq messageReq) {
        // Implement logic to send a message to a conversation
        // Example:
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("Conversation not found"));
        conversation.setDateUpdate(Instant.now());
        ChatMessage chatMessage = ChatMessage.builder()
                        .conversation(conversation)
                                .sender(userService.findUserByUsername(messageReq.getUsername()))
                                        .dateSent(Instant.now())
                                                .content(messageReq.getContent())
                                                        .states(List.of(MessageState.DELIVERED))
                                                                .dateDelivered(Instant.now())
                                                                        .build();
        ChatMessage savedChatMessage = chatMessageRepository.save(chatMessage);
        conversationRepository.save(conversation);
        return messageMapper.apply(savedChatMessage);
    }
    @Override
    public Conversation updateConversationById(UUID id) {
        Optional<Conversation> conversation = conversationRepository.findById(id);
//        Code to update conversation here
        conversationRepository.save(conversation.get());
        return conversation.get();
    }

//    Chatting features

}
