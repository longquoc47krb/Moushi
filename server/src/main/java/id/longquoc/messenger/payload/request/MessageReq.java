package id.longquoc.messenger.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;
@Data
@AllArgsConstructor
public class MessageReq {
    private String username;
    private UUID conversationId;
    private String content;
    private String image;
}
