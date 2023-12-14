package id.longquoc.messenger.mapper;

import id.longquoc.messenger.enums.Role;
import id.longquoc.messenger.model.User;
import id.longquoc.messenger.security.service.UserDetailsImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class UserMapper {
    public User mapUserDetailToUser(UserDetailsImpl userDetail) {
        return mapUserDetailToUser(userDetail, true);
    }
    public User mapUserDetailToUser(UserDetailsImpl userDetail, boolean ignorePassword) {
        User user = new User();
        user.setId(userDetail.getId());
        user.setFullName(userDetail.getFullName());
        user.setUsername(userDetail.getUsername());
        user.setEmail(userDetail.getEmail());
        if(!ignorePassword){
            user.setPassword(userDetail.getPassword());
        }
        // Assuming the authorities in UserDetail should be mapped to roles in User
        List<Role> roles = new ArrayList<>();
        if (userDetail.getAuthorities() != null) {
            for (GrantedAuthority authority : userDetail.getAuthorities()) {
                roles.add(Role.valueOf(authority.getAuthority()));
            }
        }
        user.setRoles(roles);
        return user;
    }
    public List<User> mapperToUserIgnorePasswordField(List<User> userList){
        return userList.stream().map(this::mapperToUserIgnorePasswordField).toList();
    }
    public User mapperToUserIgnorePasswordField(User user){
        return new User(user.getId(),user.getFullName(), user.getUsername(), user.getEmail(), user.getRoles());
    }
}
