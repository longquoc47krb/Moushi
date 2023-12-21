package id.longquoc.messenger.service;

import id.longquoc.messenger.dto.FriendRequestDto;
import id.longquoc.messenger.enums.FriendshipRole;
import id.longquoc.messenger.mapper.FriendRequestMapper;
import id.longquoc.messenger.model.FriendRequest;
import id.longquoc.messenger.model.User;
import id.longquoc.messenger.enums.FriendRequestStatus;
import id.longquoc.messenger.payload.response.AreFriendShip;
import id.longquoc.messenger.payload.response.ResponseObject;
import id.longquoc.messenger.repository.FriendRequestRepository;
import id.longquoc.messenger.repository.UserRepository;
import id.longquoc.messenger.service.interfaces.IFriendRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FriendRequestService implements IFriendRequestService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FriendRequestRepository friendRequestRepository;
    @Autowired
    private FriendRequestMapper friendRequestMapper;

    public ResponseEntity<?> sendFriendInvitation(UUID senderId, UUID receiverId) {
        User sender = userRepository.findById(senderId).orElseThrow(() -> new ResolutionException("User not found"));
        User receiver = userRepository.findById(receiverId).orElseThrow(() -> new ResolutionException("User not found"));
        if (senderId.equals(receiverId)) {
            return ResponseEntity.badRequest().body(new ResponseObject(HttpStatus.BAD_REQUEST.value(), "Cannot send friend request to yourself"));
        }
        if (areFriends(sender, receiver)) {
            return ResponseEntity.badRequest().body(new ResponseObject(HttpStatus.BAD_REQUEST.value(), "Users are already friendship", new AreFriendShip(true)));
        }
        var isSent = friendRequestRepository.findBySenderAndReceiverAndStatus(sender, receiver, FriendRequestStatus.PENDING);
        if(!isSent.isEmpty()) {
            return ResponseEntity.badRequest().body(new ResponseObject(HttpStatus.BAD_REQUEST.value(), "Friend request has sent already"));
        }
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSender(sender);
        friendRequest.setReceiver(receiver);
        friendRequest.setStatus(FriendRequestStatus.PENDING);

        friendRequestRepository.save(friendRequest);
        return ResponseEntity.ok().body(new ResponseObject(HttpStatus.ACCEPTED.value(), "Sent request successfully", friendRequest));
    }
    public void acceptFriendRequest(Long requestId) {
        FriendRequest friendRequest = friendRequestRepository.findById(requestId).orElseThrow(() -> new ResolutionException("Friend request not found"));
        friendRequest.setStatus(FriendRequestStatus.ACCEPTED);
        friendRequest.setRole(FriendshipRole.BASIC);
        friendRequestRepository.save(friendRequest);
    }
    public boolean areFriends(User account1, User account2) {
        return !friendRequestRepository.findBySenderAndReceiverAndStatus(account1, account2, FriendRequestStatus.ACCEPTED).isEmpty()
                || !friendRequestRepository.findBySenderAndReceiverAndStatus(account2, account1, FriendRequestStatus.ACCEPTED).isEmpty();
    }

    @Override
    public List<FriendRequest> findByReceiverAndStatus(User receiver, FriendRequestStatus status) {
        return friendRequestRepository.findByReceiverAndStatus(receiver, status);
    }

    @Override
    public List<FriendRequest> findBySenderAndStatus(User sender, FriendRequestStatus status) {
        return friendRequestRepository.findBySenderAndStatus(sender, status);
    }

    @Override
    public FriendRequest findByReceiver(User receiver) {
        return friendRequestRepository.findByReceiver(receiver);
    }

    @Override
    public List<FriendRequestDto> findFriendRequestsByUserId(UUID userId) {
        User user = userRepository.findById(userId).get();
        List<FriendRequest> friendRequests = friendRequestRepository.findByUserAsSenderOrReceiver(user);
        List<FriendRequest> friendRequestList = friendRequestRepository.findAll();
        return friendRequests.stream()
                .map(fr -> friendRequestMapper.toFriendRequestDto(fr))
                .collect(Collectors.toList());
    }
}
