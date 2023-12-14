package id.longquoc.messenger.controller;

import id.longquoc.messenger.model.FriendRequest;
import id.longquoc.messenger.model.User;
import id.longquoc.messenger.enums.FriendRequestStatus;
import id.longquoc.messenger.payload.request.FriendRequestDto;
import id.longquoc.messenger.payload.response.ResponseObject;
import id.longquoc.messenger.service.FriendRequestService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/v1/api/friendship")
public class FriendRequestController {
    @Autowired
    private FriendRequestService friendService;
    @PostMapping("/requests")
    public ResponseEntity<?> sendFriendRequest(@RequestBody FriendRequestDto friendRequestDto) throws BadRequestException {
        return friendService.addFriendShip(friendRequestDto.getSenderId(), friendRequestDto.getReceiverId());

    }

    public List<FriendRequest> findByReceiverAndStatus(User receiver, FriendRequestStatus status) {
        return friendService.findByReceiverAndStatus(receiver, status);
    }

    public List<FriendRequest> findBySenderAndStatus(User sender, FriendRequestStatus status) {
        return friendService.findBySenderAndStatus(sender, status);
    }
    @PutMapping("/accept/{requestId}")
    public ResponseEntity<?> acceptFriendRequest(@PathVariable Long requestId) throws BadRequestException {
        friendService.acceptFriendRequest(requestId);
        return ResponseEntity.ok().body(new ResponseObject(201, "Accepted successfully"));
    }
}
