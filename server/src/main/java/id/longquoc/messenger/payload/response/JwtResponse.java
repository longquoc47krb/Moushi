package id.longquoc.messenger.payload.response;

import id.longquoc.messenger.model.User;
import id.longquoc.messenger.security.service.UserDetailsImpl;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class JwtResponse {
    private String accessToken;
    private User user;
}
