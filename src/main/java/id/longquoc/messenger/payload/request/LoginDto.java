package id.longquoc.messenger.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginDto {
    @NotNull(message = "Credential is required")
    private String credential;
    @NotNull(message = "Password is required")
    private String password;
}
