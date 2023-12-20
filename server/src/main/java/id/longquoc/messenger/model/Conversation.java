package id.longquoc.messenger.model;

import id.longquoc.messenger.enums.ConversationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_conversation",
            joinColumns = @JoinColumn(name = "conversation_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> participants;
    @OneToMany(mappedBy = "conversation")
    private List<Message> messages;
    private boolean groupConversation = false;
    @NotNull
    private Instant dateStarted;
    @Enumerated(EnumType.STRING)
    private ConversationStatus status;

    public void setStatusBasedOnFriendship(boolean areFriendship){
        setStatus(areFriendship ? ConversationStatus.ACCEPTED : ConversationStatus.PENDING);
    }
    public List<String> getUsernames() {
        List<String> usernames = new ArrayList<>();
        for (User user : participants) {
            usernames.add(user.getUsername());
        }
        return usernames;
    }

    public Conversation(List<User> participants) {
        this.participants = participants;
        this.dateStarted = Instant.now();
    }
}
