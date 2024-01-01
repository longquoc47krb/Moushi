package id.longquoc.messenger.service.interfaces;

import id.longquoc.messenger.model.User;
import id.longquoc.messenger.security.service.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.UUID;

public interface IUserService {
    List<User> findAllUser();
    User findByEmailOrUsername(String email, String username);
    User findUserdById(UUID id);
    List<User> findUsersById(List<UUID> participants);
    List<User> findUsersInConversation(UUID conversationId);
    UserDetails currentUser(HttpServletRequest request);
    boolean deleteUserByEmail(String email);
    List<User> getFriendList(UUID userId);
}
