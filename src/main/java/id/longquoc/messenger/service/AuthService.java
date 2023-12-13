package id.longquoc.messenger.service;

import id.longquoc.messenger.dto.LoginDto;
import id.longquoc.messenger.dto.RegisterDto;
import id.longquoc.messenger.model.Role;
import id.longquoc.messenger.model.User;
import id.longquoc.messenger.payload.response.ResponseObject;
import id.longquoc.messenger.repository.UserRepository;
import id.longquoc.messenger.service.interfaces.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public ResponseEntity<?> registerUser(RegisterDto registerDto) {
        if(userRepository.existsByEmail(registerDto.getEmail())){
            return ResponseEntity.badRequest().body(new ResponseObject(400,"Email existed", null ));
        }
        User user = new User(registerDto.getFullName(), registerDto.getUsername(), passwordEncoder.encode(registerDto.getPassword()), registerDto.getEmail());
        try {
            user.setRoles(Arrays.asList(Role.ROLE_BASIC));
            userRepository.save(user);
            return ResponseEntity.ok(new ResponseObject(200, "Register new account successfully", user));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(new ResponseObject(500, "Register failed", null));
        }
    }


}
