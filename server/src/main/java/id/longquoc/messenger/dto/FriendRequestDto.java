package id.longquoc.messenger.dto;

import id.longquoc.messenger.enums.FriendRequestStatus;
import id.longquoc.messenger.model.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FriendRequestDto {
    private User sender;
    private User receiver;
    private FriendRequestStatus status;
}
