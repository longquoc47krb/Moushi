package id.longquoc.messenger.dto.user;

import id.longquoc.messenger.enums.TypingState;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStateDto {
    private String username;
    private Instant lastOnline;
    private TypingState typingState;
}
