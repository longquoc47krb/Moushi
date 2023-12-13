package id.longquoc.messenger.controller;

import id.longquoc.messenger.model.User;
import id.longquoc.messenger.payload.response.ResponseObject;
import id.longquoc.messenger.service.UserService;
import id.longquoc.messenger.service.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        if(user == null){
            return ResponseEntity.badRequest().body(new ResponseObject(404, "User not found", null));
        }
        return ResponseEntity.ok(new ResponseObject(200, "Fetch user successfully", user));
    }
}
