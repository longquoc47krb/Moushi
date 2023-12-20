package id.longquoc.messenger.payload.response;

import id.longquoc.messenger.model.Message;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ConversationResponse {
    UUID id;
    List<UserResponse> participants;
    Instant dateStarted;
    List<Message> messages;
}
