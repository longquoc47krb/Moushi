package id.longquoc.messenger.controller;

import id.longquoc.messenger.payload.request.ConversationRequest;
import id.longquoc.messenger.payload.response.ConversationResponse;
import id.longquoc.messenger.payload.response.ResponseObject;
import id.longquoc.messenger.service.ConversationService;
import id.longquoc.messenger.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/v1/api/conversation")
@Slf4j
public class ConversationController {
    private final UserService userService;
    private final ConversationService conversationService;
    @GetMapping()
    public ResponseEntity<?> findAllByUserId(Authentication authentication){
        try {
            var user = authentication.getName();
            var userEntity = userService.findByEmailOrUsername(user,user);
            List<ConversationResponse> responseList = conversationService.getAllConversations(userEntity.getId());
            return ResponseEntity.ok(new ResponseObject(200, "Fetch conversations successfully", responseList));
        } catch (Exception ex) {
           return ResponseEntity.badRequest().body(new ResponseObject(404, "Conversations not found")) ;

        }

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
}
