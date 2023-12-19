package id.longquoc.messenger.payload.request;

import id.longquoc.messenger.enums.MessageState;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

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
