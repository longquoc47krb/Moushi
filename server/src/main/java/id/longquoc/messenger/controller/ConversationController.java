package id.longquoc.messenger.controller;

import id.longquoc.messenger.model.Conversation;
import id.longquoc.messenger.payload.request.ConversationRequest;
import id.longquoc.messenger.payload.request.MessageReq;
import id.longquoc.messenger.payload.response.ConversationResponse;
import id.longquoc.messenger.payload.response.MessageResponse;
import id.longquoc.messenger.payload.response.ResponseObject;
import id.longquoc.messenger.repository.ConversationRepository;
import id.longquoc.messenger.service.ChatMessageService;
import id.longquoc.messenger.service.ConversationService;
import id.longquoc.messenger.service.UserService;
import id.longquoc.messenger.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/v1/api/conversation")
@Slf4j
public class ConversationController {
    private final UserService userService;
    private final ChatService chatService;
    private final ConversationService conversationService;
    private final ConversationRepository conversationRepository;
    private final ChatMessageService messageService;
    @GetMapping()
    public ResponseEntity<?> findAllConversations(){
        try {
            List<ConversationResponse> conversations = conversationService.getAllConversations();
            return ResponseEntity.ok(new ResponseObject(200, "Fetch conversations successfully", conversations));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new ResponseObject(404, "Conversations not found")) ;

        }
    }
    @GetMapping("/by-userId/{userId}")
    public ResponseEntity<?> findAllByUserId(@PathVariable String userId){
        try {
            List<ConversationResponse> responseList = conversationService.getAllConversationsByUserId(UUID.fromString(userId));
            return ResponseEntity.ok(new ResponseObject(200, "Fetch conversations successfully", responseList));
        } catch (Exception ex) {
           return ResponseEntity.badRequest().body(new ResponseObject(404, ex.getMessage())) ;

        }

    }
    @PutMapping("/by-id/{id}")
    public ResponseEntity<?> updateConversationById(@PathVariable String id){
        Conversation conversation = conversationService.updateConversationById(UUID.fromString(id));
        if(conversation == null){
            return ResponseEntity.badRequest().body(new ResponseObject(400, "Conversation not found"));
        }
        return ResponseEntity.ok().body(new ResponseObject(200,"Fetch conversation successfully", conversation));

    }
    @GetMapping("/by-id/{id}")
    public ResponseEntity<?> findConversationById(@PathVariable String id){
        ConversationResponse conversation = conversationService.getConversationById(UUID.fromString(id));
        if(conversation == null){
            return ResponseEntity.badRequest().body(new ResponseObject(400, "Conversation not found"));
        }
        return ResponseEntity.ok().body(new ResponseObject(200,"Fetch conversation successfully", conversation));

    }
    @GetMapping("/message/by-id/{id}")
    public ResponseEntity<?> getMessageListByConversationId(@PathVariable String id){
        List<MessageResponse> chatMessages = conversationService.getMessages(id);
        if(chatMessages == null){
            return ResponseEntity.badRequest().body(new ResponseObject(400, "chatMessages not found"));
        }
        return ResponseEntity.ok().body(new ResponseObject(200,"Fetch chatMessages successfully", chatMessages));

    }

    @PostMapping("/send-message/{conversationId}")
    public ResponseEntity<MessageResponse> sendMessage(
            @PathVariable UUID conversationId,
            @RequestBody MessageReq message
    ) {
        MessageResponse sentMessage = conversationService.sendMessage(conversationId, message);
        return new ResponseEntity<>(sentMessage, HttpStatus.CREATED);
    }
    @PostMapping("/create")
    public ResponseEntity<?> createConversation(@RequestBody ConversationRequest conversationRequest)  {
        log.info(conversationRequest.toString());
        var conversation = conversationService.createConversation(conversationRequest);
        if(conversation == null) {
            return ResponseEntity.badRequest().body(new ResponseObject(500, "Create failed"));
        }
        return ResponseEntity.ok(new ResponseObject(201, "Create new conversation successfully", conversation));
    }
//    Chatting features
    @MessageMapping("/private.chat")
    public void sendPrivateMessage(@Payload MessageReq messageReq){
        chatService.processPrivateMessage(messageReq);
    }
    @DeleteMapping("/remove-message-by-id/{id}")
    public void removeMessage(@PathVariable String id){
        messageService.removeMessageById(UUID.fromString(id));
    }
}
