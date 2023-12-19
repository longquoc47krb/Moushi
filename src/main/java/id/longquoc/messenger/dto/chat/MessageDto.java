package id.longquoc.messenger.dto.chat;

import id.longquoc.messenger.enums.MessageState;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.UUID;
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    @NotNull
    private UUID id;
    private String content;
    private byte[] image;
    private String sender;
    private String receiver;
    private String dateSent;
    private String dateDelivered;
    private String dateRead;
    private List<MessageState> states;
}
