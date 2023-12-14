package id.longquoc.messenger.service.interfaces;

import id.longquoc.messenger.payload.request.LoginDto;
import id.longquoc.messenger.payload.request.RegisterDto;
import org.springframework.http.ResponseEntity;

public interface IAuthService {
    ResponseEntity<?> registerUser(RegisterDto registerDto);
    ResponseEntity<?> login(LoginDto loginDto);
}
