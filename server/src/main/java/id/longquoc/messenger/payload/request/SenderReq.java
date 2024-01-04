package id.longquoc.messenger.payload.request;

import id.longquoc.messenger.enums.FriendRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SenderReq {
    private UUID senderId;
    private String friendRequestStatus;
    private String friendRole;
}
