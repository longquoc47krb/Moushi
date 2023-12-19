package id.longquoc.messenger.service.interfaces;

import id.longquoc.messenger.dto.ConversationDto;
import id.longquoc.messenger.dto.MessageDto;
import id.longquoc.messenger.dto.user.OnlineUserDto;
import id.longquoc.messenger.dto.user.UserStateDto;
import id.longquoc.messenger.model.User;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

import java.util.List;

public interface IChatService {
    void submitMessage(MessageDto messageDto, StompHeaderAccessor accessor);
    List<OnlineUserDto> fetchOnlineUsers();

    List<ConversationDto> fetchConversations(String name);
    void createAndSendConversation(User[] users, StompHeaderAccessor accessor);
    void changeUserState(UserStateDto userStateDto, String username);
    MessageDto saveOrUpdateMessage(User[] users, MessageDto message);


}
