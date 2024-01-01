package id.longquoc.messenger.service;

import com.github.javafaker.Faker;
import id.longquoc.messenger.dto.FriendRequestDto;
import id.longquoc.messenger.enums.FriendRequestStatus;
import id.longquoc.messenger.mapper.UserMapper;
import id.longquoc.messenger.model.Conversation;
import id.longquoc.messenger.model.FriendRequest;
import id.longquoc.messenger.payload.request.RegisterDto;
import id.longquoc.messenger.enums.Role;
import id.longquoc.messenger.model.User;
import id.longquoc.messenger.payload.response.ResponseObject;
import id.longquoc.messenger.repository.ConversationRepository;
import id.longquoc.messenger.repository.UserRepository;
import id.longquoc.messenger.security.jwt.JwtUtils;
import id.longquoc.messenger.security.service.UserDetailsImpl;
import id.longquoc.messenger.service.interfaces.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final Faker faker;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ConversationRepository conversationRepository;
    private final JwtUtils jwtUtils;
    private final FriendRequestService friendRequestService;
    public ResponseEntity<?> generateFakeUser() {
        User user = new User();
        user.setFullName(faker.name().fullName());
        user.setUsername(faker.name().username());
        user.setEmail(faker.name().username() + "@gmail.com");
        user.setPassword("Password#12345");
        user.setProfilePicture(faker.avatar().image());
        user.setRoles(List.of(Role.ROLE_BASIC));

        try {
            userRepository.save(user);
            //            TODO: Mail service error
            //            mailService.sendRegistrationSuccessEmail(registerDto.getEmail(), registerDto.getUsername());
            return ResponseEntity.ok(new ResponseObject(200, "Register new account successfully", user));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(new ResponseObject(500, "Register failed", null));
        }
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
    public UserDetails currentUser(HttpServletRequest request) {
        var jwt = jwtUtils.getJwtFromHeader(request);
        var email = jwtUtils.getEmailFromJwt(jwt);
        User currentUser = userRepository.findByEmail(email);
        if(currentUser != null){
            return userMapper.toUserDetails(currentUser);
        }
        return null;
    }

    @Override
    public boolean deleteUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        userRepository.delete(user);
        return !userRepository.existsByEmail(email);
    }

    @Override
    public List<User> getFriendList(UUID userId) {
        User user = userRepository.findById(userId).orElse(null);
        List<FriendRequestDto> friendRequests = friendRequestService.findFriendRequestsByUserId(userId);
        List<FriendRequestDto> acceptedRequests = friendRequests.stream().filter(fr -> fr.getStatus().equals(FriendRequestStatus.ACCEPTED)).toList();

        return acceptedRequests.stream()
                .map(request -> {
                    if (request.getReceiver().equals(user)) {
                        return request.getSender();
                    } else {
                        return request.getReceiver();
                    }
                }).collect(Collectors.toList());
    }

}
