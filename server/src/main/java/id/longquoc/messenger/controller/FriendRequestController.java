package id.longquoc.messenger.controller;

import id.longquoc.messenger.dto.FriendRequestDto;
import id.longquoc.messenger.model.FriendRequest;
import id.longquoc.messenger.model.User;
import id.longquoc.messenger.enums.FriendRequestStatus;
import id.longquoc.messenger.payload.request.FriendRequestReq;
import id.longquoc.messenger.payload.request.SenderReq;
import id.longquoc.messenger.payload.response.ResponseObject;
import id.longquoc.messenger.service.FriendRequestService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/v1/api/friendship")
public class FriendRequestController {
    @Autowired
    private FriendRequestService friendService;
    @PostMapping("/requests")
    public ResponseEntity<?> sendFriendRequest(@RequestBody FriendRequestReq friendRequestDto) throws BadRequestException {
        return friendService.sendFriendInvitation(friendRequestDto.getSenderId(), friendRequestDto.getReceiverId());

    }
    @GetMapping("/by-userId/{userId}")
    public List<FriendRequestDto> findFriendRequestsByUserId(@PathVariable String userId) {
        return friendService.findFriendRequestsByUserId(UUID.fromString(userId));
    }

    public List<FriendRequest> findBySenderAndStatus(User sender, FriendRequestStatus status) {
        return friendService.findBySenderAndStatus(sender, status);
    }
    @PutMapping("/accept/{requestId}")
    public ResponseEntity<?> acceptFriendRequest(@PathVariable Long requestId, @RequestBody SenderReq senderReq) throws BadRequestException {
       return friendService.acceptFriendRequest(requestId, senderReq);
    }
    @DeleteMapping("/{friendRequestId}")
    public ResponseEntity<String> deleteFriendRequest(@PathVariable String friendRequestId) {
        friendService.deleteFriendRequestById(Long.parseLong(friendRequestId));
        return ResponseEntity.ok("Friend request deleted successfully");
    }
    @GetMapping("/by-id/{requestId}")
    public ResponseEntity<?> findFriendRequestById(@PathVariable String requestId) {
        var friendRequest = friendService.findFriendRequestById(Long.parseLong(requestId));
        if( friendRequest != null ) {
            return ResponseEntity.ok(new ResponseObject(200, "Fetch friend request successfully", friendRequest));
        }
        return ResponseEntity.badRequest().body(new ResponseObject(400, "Failed fetch"));
    }
}
