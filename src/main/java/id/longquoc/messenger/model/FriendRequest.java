package id.longquoc.messenger.model;

import id.longquoc.messenger.enums.FriendRequestStatus;
import id.longquoc.messenger.enums.FriendshipRole;
import jakarta.persistence.*;
import lombok.Data;
@Entity
@Data
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "sender")
    private User sender;
    @ManyToOne
    @JoinColumn(name = "receiver")
    private User receiver;
    @Enumerated(EnumType.STRING)
    private FriendRequestStatus status;
    @Enumerated(EnumType.STRING)
    private FriendshipRole role = FriendshipRole.BASIC;

}
