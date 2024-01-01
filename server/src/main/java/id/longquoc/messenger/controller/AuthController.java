package id.longquoc.messenger.controller;

import id.longquoc.messenger.payload.request.RegisterDto;
import id.longquoc.messenger.payload.request.LoginDto;
import id.longquoc.messenger.service.UserService;
import id.longquoc.messenger.service.interfaces.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @PostMapping(value = "/logout")
    void logout() {
        throw new IllegalStateException("Add Spring Security to handle authentication");
    }

}
