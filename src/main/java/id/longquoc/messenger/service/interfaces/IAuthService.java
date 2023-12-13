package id.longquoc.messenger.service.interfaces;

import id.longquoc.messenger.dto.LoginDto;
import id.longquoc.messenger.dto.RegisterDto;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;

public interface IAuthService {
    ResponseEntity<?> registerUser(RegisterDto registerDto);
}
