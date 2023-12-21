package id.longquoc.messenger.service;

import com.github.javafaker.Faker;
import id.longquoc.messenger.mapper.UserMapper;
import id.longquoc.messenger.model.Conversation;
import id.longquoc.messenger.payload.request.RegisterDto;
import id.longquoc.messenger.enums.Role;
import id.longquoc.messenger.model.User;
import id.longquoc.messenger.repository.ConversationRepository;
import id.longquoc.messenger.repository.UserRepository;
import id.longquoc.messenger.service.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final Faker faker;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ConversationRepository conversationRepository;
    public RegisterDto generateFakeUser() {
        RegisterDto user = new RegisterDto();
        user.setFullName(faker.name().fullName());
        user.setUsername(faker.name().username());
        user.setEmail(faker.name().username() + "@gmail.com");
        user.setPassword(faker.crypto().md5());
        user.setRoles(List.of(Role.ROLE_BASIC));
        return user;
    }


    @Override
    public List<User> findAllUser() {
        return userMapper.mapperToUserIgnorePasswordField(userRepository.findAll());
    }

    @Override
    public User findByEmailOrUsername(String email, String username) {
        return userMapper.mapperToUserIgnorePasswordField(userRepository.findByEmailOrUsername(email, username));
    }
    @Override
    public User findUserdById(UUID id) {
        return userRepository.findById(id).orElseThrow( () -> new UsernameNotFoundException("User not found"));
    }
    public User findUserdByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findUsersById(List<UUID> participants) {
        List<User> userList = new ArrayList<User>();
        participants.forEach(
                participant -> {
                    if(this.findUserdById(participant) == null) {
                        throw new IllegalArgumentException("User not found");
                    }else{
                        userList.add(this.findUserdById(participant));
                    }

                }
        );
        return userList;
    }

    @Override
    public List<User> findUsersInConversation(UUID conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId).get();
        return conversation.getParticipants();
    }

    @Override
    public boolean deleteUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        userRepository.delete(user);
        return !userRepository.existsByEmail(email);
    }

}
