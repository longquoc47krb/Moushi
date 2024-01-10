package id.longquoc.messenger.model;

import id.longquoc.messenger.enums.MessageState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;
    private String content;
    private String image;
    private Instant createdAt;

    private Instant dateSent;
    private Instant dateDelivered;
    private Instant dateRead;
    @Enumerated(EnumType.STRING)
    private List<MessageState> states;

    public static ChatMessage from(User sender, Conversation conversation, String content) {
        ChatMessage message = new ChatMessage();
        message.setContent(content);
        message.setConversation(conversation);
        message.setSender(sender);
        message.setDateSent(Instant.now());
        return message;
    }
}
