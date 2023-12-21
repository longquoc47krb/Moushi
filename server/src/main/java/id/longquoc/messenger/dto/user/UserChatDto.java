package id.longquoc.messenger.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;
@Data
@AllArgsConstructor
public class UserChatDto {
    private UUID id;
    private String username;
}
