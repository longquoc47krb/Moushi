package id.longquoc.messenger.service.interfaces;

import id.longquoc.messenger.payload.request.LoginDto;
import id.longquoc.messenger.payload.request.RegisterDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface IAuthService {
    ResponseEntity<?> registerUser(RegisterDto registerDto);
    ResponseEntity<?> login(LoginDto loginDto);
    Authentication authenticateUserFromHeaderAuth(String authHeader);
}
