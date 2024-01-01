package id.longquoc.messenger.controller;

import id.longquoc.messenger.dto.NotificationDTO;
import id.longquoc.messenger.dto.chat.ConversationDto;
import id.longquoc.messenger.dto.user.UserStateDto;
import id.longquoc.messenger.service.chat.ChatService;
import id.longquoc.messenger.service.chat.MessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;
    private final MessageSender devMessageSender;


    @MessageMapping("/chat")
    public void sendMessage(@Payload NotificationDTO notificationDTO, Principal principal){
        chatService.processMessage(notificationDTO, principal);
    }
    @MessageMapping("/conversation")
    @SendToUser(broadcast = false)
    public List<ConversationDto> sendConversations(Principal principal){
        return chatService.fetchConversations(principal.getName());
    }
    @MessageMapping("/online")
    public void onlineUsers(Message<?> message){
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//        messagingTemplate.convertAndSend("/topic/online", chatService.fetchOnlineUsers());
        messagingTemplate.convertAndSend("/topic/online", "Co 1 nguoi online");
        devMessageSender.sendReceipt(accessor, "Processed by server");
    }
    @MessageMapping("/change-user-state")
    public void changeUserState(@Payload UserStateDto userStateDto, Principal userPrincipal) {
        chatService.changeUserState(userStateDto, userPrincipal.getName());
    }

}
