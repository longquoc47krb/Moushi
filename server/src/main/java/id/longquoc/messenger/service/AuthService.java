package id.longquoc.messenger.service;

import id.longquoc.messenger.exception.UnauthorizedException;
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
import id.longquoc.messenger.security.service.UserDetailsServiceImpl;
import id.longquoc.messenger.service.interfaces.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor

public class AuthService implements IAuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserMapper userMapper;
    private final MailService mailService;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
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
            user.setRoles(List.of(Role.ROLE_BASIC));
            userRepository.save(user);
            //            TODO: Mail service error
            //            mailService.sendRegistrationSuccessEmail(registerDto.getEmail(), registerDto.getUsername());
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

    @Override
    public Authentication authenticateUserFromHeaderAuth(String headerAuth) {
        String bearer = "Bearer ";
        if(!StringUtils.hasText(headerAuth) || !headerAuth.startsWith(bearer)){
            throw new UnauthorizedException("Incomplete or missing authentication headers");
        }
        var jwtToken = headerAuth.substring(bearer.length());
        var email = jwtUtils.getEmailFromJwt(jwtToken);
        if((email != null || SecurityContextHolder.getContext().getAuthentication() != null) && jwtUtils.validateJwtToken(jwtToken)){
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        return SecurityContextHolder.getContext().getAuthentication();
    }



}
