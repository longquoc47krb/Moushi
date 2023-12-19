package id.longquoc.messenger.payload.response;

import id.longquoc.messenger.model.Message;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ConversationResponse {
    UUID id;
    String name;
    List<UserResponse> participants;
    List<Message> messages;
}
