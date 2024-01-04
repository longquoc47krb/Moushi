package id.longquoc.messenger.controller;

import id.longquoc.messenger.model.User;
import id.longquoc.messenger.payload.request.PasswordDto;
import id.longquoc.messenger.payload.response.FriendInvitationResponse;
import id.longquoc.messenger.payload.response.ResponseObject;
import id.longquoc.messenger.service.interfaces.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@Slf4j
@RestController
@RequestMapping(path = "/v1/api/user")
@RequiredArgsConstructor
public class UserController {
    private final IUserService iUserService;
    @GetMapping()
    public ResponseEntity<?> findAllUsers(){
        List<User> users = iUserService.findAllUser();
        if(users.isEmpty()) {
            return ResponseEntity.badRequest().body(new ResponseObject(404, "Users not found", null));
        }
        return ResponseEntity.ok(new ResponseObject(200, "Fetch all users successfully", users));
    }

    @GetMapping("/by-email-or-username")
    public ResponseEntity<?> findByEmailOrUsername(@RequestParam(value = "email", required = false) String email, @RequestParam(value = "username", required = false) String username ){
        User user = iUserService.findByEmailOrUsername(email, username);
        SecurityContext holder = SecurityContextHolder.getContext();
        log.info(holder.toString());
        if(user == null){
            return ResponseEntity.badRequest().body(new ResponseObject(404, "User not found", null));
        }
        return ResponseEntity.ok(new ResponseObject(200, "Fetch user successfully", user));
    }
    @GetMapping("/find-user-in-conversation/{conversationId}")
    public ResponseEntity<?> findUsersInConversation(@PathVariable String conversationId) {
        List<User> users = iUserService.findUsersInConversation(UUID.fromString(conversationId));
        return ResponseEntity.ok(new ResponseObject(200, "Fetch users successfully", users));

    }
    @DeleteMapping("/{email}")
    public ResponseEntity<?> deleteUserByEmail(@PathVariable String email){
        var deleted = iUserService.deleteUserByEmail(email);
        if(deleted){
            return ResponseEntity.ok(new ResponseObject(200, "Deleted successfully"));
        }
        return ResponseEntity.badRequest().body(new ResponseObject(500, "Failed deleted"));

    }
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        var user = iUserService.currentUser(request);
        if(user != null){
            return ResponseEntity.ok(new ResponseObject(200, "Get current user successfully", user));
        }
        return ResponseEntity.badRequest().body(new ResponseObject(500, "Failed fetch"));

    }
    @GetMapping("/friend-list/{userId}")
    public ResponseEntity<?> getFriendList(@PathVariable String userId) throws BadRequestException {
        List<User> friends = iUserService.getFriendList(UUID.fromString(userId));
        return ResponseEntity.ok().body(new ResponseObject(200, "Fetch friends successfully", friends));
    }
    @SneakyThrows
    @GetMapping("/friend-requests/{userId}")
    public ResponseEntity<?> getFriendRequests(@PathVariable String userId) {
        List<FriendInvitationResponse> friendRequestList = iUserService.getFriendRequestList(UUID.fromString(userId));
        return ResponseEntity.ok().body(new ResponseObject(200, "Fetch friend requests successfully", friendRequestList));

    }
    @PutMapping("/update-password/{userId}")
    public ResponseEntity<?> updatePassword(@PathVariable String userId, @RequestBody PasswordDto passwordDto){
        User user = iUserService.updateNewPassword(UUID.fromString(userId), passwordDto.getNewPassword());
        if(user == null){
            return ResponseEntity.badRequest().body(new ResponseObject(400, "Update password failed"));
        }
        return ResponseEntity.ok().body(new ResponseObject(200, "Update password successfully", user));
    }
}
