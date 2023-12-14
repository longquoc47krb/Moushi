package id.longquoc.messenger.service.interfaces;

import id.longquoc.messenger.model.FriendRequest;
import id.longquoc.messenger.model.User;
import id.longquoc.messenger.enums.FriendRequestStatus;

import java.util.List;

public interface IFriendRequestService {
    List<FriendRequest> findByReceiverAndStatus(User receiver, FriendRequestStatus status);

    List<FriendRequest> findBySenderAndStatus(User sender, FriendRequestStatus status);
    FriendRequest findByReceiver(User receiver);
}
