package id.longquoc.messenger.service;

import com.github.javafaker.Faker;
import id.longquoc.messenger.dto.RegisterDto;
import id.longquoc.messenger.model.Role;
import id.longquoc.messenger.model.User;
import id.longquoc.messenger.repository.UserRepository;
import id.longquoc.messenger.service.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final Faker faker;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;
    private final UserRepository userRepository;
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
        return userRepository.findAll();
    }

    @Override
    public User findByEmailOrUsername(String email, String username) {
        return userRepository.findByEmailOrUsername(email, username);
    }
}
