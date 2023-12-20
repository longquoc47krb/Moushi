package id.longquoc.messenger.controller;

import id.longquoc.messenger.dto.chat.ConversationDto;
import id.longquoc.messenger.dto.chat.MessageDto;
import id.longquoc.messenger.dto.user.UserStateDto;
import id.longquoc.messenger.model.Conversation;
import id.longquoc.messenger.service.ConversationService;
import id.longquoc.messenger.service.chat.ChatService;
import id.longquoc.messenger.service.chat.MessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;
    private final MessageSender devMessageSender;

    @MessageMapping("/conversation/{conversationId}")
    public void sendMessage(@DestinationVariable String id, @Payload MessageDto payload, Message<?> message){
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        chatService.submitMessage(payload, accessor);
        devMessageSender.sendReceipt(accessor, "Processed by server");
    }
    @MessageMapping("/conversation")
    @SendToUser(broadcast = false)
    public List<ConversationDto> sendConversations(Principal principal){
        return chatService.fetchConversations(principal.getName());
    }
    @MessageMapping("/online")
    public void onlineUsers(Message<?> message){
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        messagingTemplate.convertAndSend("/topic/online", chatService.fetchOnlineUsers());
        devMessageSender.sendReceipt(accessor, "Processed by server");
    }
    @MessageMapping("/change-user-state")
    public void changeUserState(@Payload UserStateDto userStateDto, Principal userPrincipal) {
        chatService.changeUserState(userStateDto, userPrincipal.getName());
    }

}
