package id.longquoc.messenger.controller;

import id.longquoc.messenger.payload.request.RegisterDto;
import id.longquoc.messenger.payload.request.LoginDto;
import id.longquoc.messenger.service.UserService;
import id.longquoc.messenger.service.interfaces.IAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/v1/api/auth")
@RequiredArgsConstructor
public class AuthController {
    @Autowired
    private final IAuthService iAuthService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto){
        return iAuthService.registerUser(registerDto);
    }
    @PostMapping("/register/fake")
    public ResponseEntity<?> registerFake(@RequestBody RegisterDto registerDto){
        return userService.generateFakeUser();
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto){
        return iAuthService.login(loginDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        // Here, you might invalidate the session or token based on your authentication mechanism.
        // For example, if you're using Spring Security with sessions:
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, null, auth);
            SecurityContextHolder.clearContext(); // Clear security context
        }

        return ResponseEntity.ok("Logged out successfully");
    }
}
