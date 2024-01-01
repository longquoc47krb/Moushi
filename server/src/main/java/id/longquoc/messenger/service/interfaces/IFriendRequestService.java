package id.longquoc.messenger.service.interfaces;

import id.longquoc.messenger.dto.FriendRequestDto;
import id.longquoc.messenger.model.FriendRequest;
import id.longquoc.messenger.model.User;
import id.longquoc.messenger.enums.FriendRequestStatus;

import java.util.List;
import java.util.UUID;

public interface IFriendRequestService {
    List<FriendRequest> findByReceiverAndStatus(User receiver, FriendRequestStatus status);

    List<FriendRequest> findBySenderAndStatus(User sender, FriendRequestStatus status);
    FriendRequest findByReceiver(User receiver);
    FriendRequest findFriendRequestById(Long id);
    List<FriendRequestDto> findFriendRequestsByUserId(UUID userId);
    void deleteFriendRequestById(Long requestId);
}
