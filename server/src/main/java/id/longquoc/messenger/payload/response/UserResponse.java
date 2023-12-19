package id.longquoc.messenger.payload.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class UserResponse {
    String fullName;
    UUID id;
}
