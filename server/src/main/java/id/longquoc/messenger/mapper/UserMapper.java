package id.longquoc.messenger.mapper;

import id.longquoc.messenger.enums.Role;
import id.longquoc.messenger.model.User;
import id.longquoc.messenger.payload.response.UserResponse;
import id.longquoc.messenger.payload.response.UserResponseSecure;
import id.longquoc.messenger.security.service.UserDetailsImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
        user.setProfilePicture(userDetail.getProfilePicture());
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
    public UserResponse mapUserToUserResponse(User user){
        return UserResponse.builder().fullName(user.getFullName()).id(user.getId()).build();
    }
    public List<UserResponse> mapUsers(List<User> users) {
        return users.stream()
                .map(this::mapUserToUserResponse)
                .collect(Collectors.toList());
    }
    public List<UUID> toListUUID(List<User> users){
        return users.stream()
                .map(User::getId)
                .collect(Collectors.toList());
    }
    public UserDetails toUserDetails(User user) {
        return UserDetailsImpl.build(user);
    }
    public UserResponseSecure toUserResponseSecure(User user){
        return UserResponseSecure.builder()
                .id(user.getId())
                .email(user.getEmail())
                .phone(user.getPhone())
                .userState(user.getUserState())
                .profilePicture(user.getProfilePicture())
                .username(user.getUsername())
                .lastOnline(user.getLastOnline())
                .roles(user.getRoles())
                .fullName(user.getFullName())
                .build();
    }
}
