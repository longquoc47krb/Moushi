package id.longquoc.messenger.service;

import id.longquoc.messenger.mapper.UserMapper;
import id.longquoc.messenger.payload.request.LoginDto;
import id.longquoc.messenger.payload.request.RegisterDto;
import id.longquoc.messenger.enums.Role;
import id.longquoc.messenger.model.User;
import id.longquoc.messenger.payload.response.JwtResponse;
import id.longquoc.messenger.payload.response.ResponseObject;
import id.longquoc.messenger.repository.UserRepository;
import id.longquoc.messenger.security.jwt.JwtUtils;
import id.longquoc.messenger.security.service.UserDetailsImpl;
import id.longquoc.messenger.service.interfaces.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    private final JwtUtils jwtUtils;
    private final UserMapper userMapper;

    @Override
    public ResponseEntity<?> registerUser(RegisterDto registerDto) {
        if(userRepository.existsByEmail(registerDto.getEmail())){
            return ResponseEntity.badRequest().body(new ResponseObject(400,"Email existed", null ));
        }
        if(userRepository.existsByUsername(registerDto.getUsername())){
            return ResponseEntity.badRequest().body(new ResponseObject(400,"Username existed", null ));
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

    @Override
    public ResponseEntity<?> login(LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getCredential(), loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);


            UserDetailsImpl userDetails = (UserDetailsImpl)  authentication.getPrincipal();
            return ResponseEntity.ok(new ResponseObject(201, "Login successfully", new JwtResponse(jwt, userMapper.mapUserDetailToUser(userDetails)
            )));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseObject(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null));
        }

    }


}
