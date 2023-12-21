package id.longquoc.messenger.controller;

import id.longquoc.messenger.dto.FriendRequestDto;
import id.longquoc.messenger.model.FriendRequest;
import id.longquoc.messenger.model.User;
import id.longquoc.messenger.enums.FriendRequestStatus;
import id.longquoc.messenger.payload.request.FriendRequestReq;
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
    @GetMapping("/{userId}")
    public List<FriendRequestDto> findFriendRequestsByUserId(@PathVariable String userId) {
        return friendService.findFriendRequestsByUserId(UUID.fromString(userId));
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
