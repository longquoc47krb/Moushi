package id.longquoc.messenger.service.chat;

import id.longquoc.messenger.dto.user.UserStateDto;
import id.longquoc.messenger.enums.UserState;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.user.SimpSession;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserSocketService {
    private final Set<UserStateDto> userSocketDTOS = new HashSet<>();
    @Autowired
    SimpUserRegistry userRegistry;
    public UserStateDto findUserSocketByUsername(String username) {
    return
            userSocketDTOS.stream()
                    .filter(userState -> userState.getUsername().equals(username))
                    .findAny()
                    .orElse(null);
    }
    public void updateStatus(String username, UserState userState) {
        UserStateDto userSocketDTO = findUserSocketByUsername(username);
        if (userSocketDTO != null) {
            userSocketDTO.setUserState(userState);
        } else {
            userSocketDTOS.add(
                    new UserStateDto(username, userState)
            );
        }
    }
    public void removeByUsername(String username) {
        userSocketDTOS.removeIf(userSocketDTO -> userSocketDTO.getUsername().equals(username));
    }
    public List<UserStateDto> getSubscribedSocketUsersByDestination(String destination) {
        return userRegistry.findSubscriptions(subscription -> subscription.getDestination().equals(destination))
                .stream()
                .map(simpSubscription -> {
                    SimpSession simpSession = simpSubscription.getSession();
                    SimpUser simpUser = simpSession.getUser();
                    UserStateDto userSocketDTO = findUserSocketByUsername(simpUser.getName());
                    if (userSocketDTO != null) {
                        return userSocketDTO;
                    }
                    return new UserStateDto(simpUser.getName(), UserState.ONLINE);
                })
                .collect(Collectors.toList());
    }
}
