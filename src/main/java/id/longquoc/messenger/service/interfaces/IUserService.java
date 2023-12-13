package id.longquoc.messenger.service.interfaces;

import id.longquoc.messenger.model.User;

import java.util.List;

public interface IUserService {
    List<User> findAllUser();
    User findByEmailOrUsername(String email, String username);
}
