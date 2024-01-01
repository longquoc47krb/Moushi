package id.longquoc.messenger.dto.chat;

import id.longquoc.messenger.enums.TypingState;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConversationDto {
    private UUID id;
    private String fullName;
    private String username;
    private String image;
    private Instant lastOnline;
    private TypingState typingState = TypingState.IDLE;
}
