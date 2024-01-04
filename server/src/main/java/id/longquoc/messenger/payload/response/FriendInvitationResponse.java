package id.longquoc.messenger.payload.response;

import id.longquoc.messenger.model.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FriendInvitationResponse {
    private Long requestId;
    private User sender;

}
