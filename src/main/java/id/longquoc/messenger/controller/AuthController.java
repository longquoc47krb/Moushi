package id.longquoc.messenger.controller;

import id.longquoc.messenger.dto.RegisterDto;
import id.longquoc.messenger.dto.LoginDto;
import id.longquoc.messenger.service.UserService;
import id.longquoc.messenger.service.interfaces.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/v1/api/auth")
@RequiredArgsConstructor
public class AuthController {
    @Autowired
    private final IAuthService iAuthService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto){
        return iAuthService.registerUser(registerDto);
    }


}
