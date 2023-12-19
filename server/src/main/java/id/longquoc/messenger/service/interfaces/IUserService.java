package id.longquoc.messenger.service.interfaces;

import id.longquoc.messenger.model.User;

import java.util.List;
import java.util.UUID;

public interface IUserService {
    List<User> findAllUser();
    User findByEmailOrUsername(String email, String username);
    User findUserdById(UUID id);
    List<User> findUsersById(List<UUID> participants);
}
