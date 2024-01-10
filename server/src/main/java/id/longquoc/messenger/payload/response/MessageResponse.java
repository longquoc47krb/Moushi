package id.longquoc.messenger.payload.response;

import id.longquoc.messenger.enums.MessageState;
import id.longquoc.messenger.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
public class MessageResponse {
    private UUID id;
    private UserResponseSecure sender;
    private String conversationId;
    private String content;
    private String image;
    private Instant dateSent;
    private Instant dateDelivered;
    private Instant dateRead;
    private List<MessageState> states;

}
