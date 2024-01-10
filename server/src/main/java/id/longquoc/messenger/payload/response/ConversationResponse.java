package id.longquoc.messenger.payload.response;

import id.longquoc.messenger.model.ChatMessage;
import id.longquoc.messenger.model.User;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ConversationResponse {
    private UUID id;
    private List<User> participants;
    private Instant dateStarted;
    private List<MessageResponse> messages;
    private boolean groupConversation;
    private Instant dateUpdate;
}
