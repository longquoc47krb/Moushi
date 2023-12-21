package id.longquoc.messenger.mapper;

import id.longquoc.messenger.dto.FriendRequestDto;
import id.longquoc.messenger.model.FriendRequest;
import id.longquoc.messenger.model.User;
import id.longquoc.messenger.payload.response.UserResponse;
import id.longquoc.messenger.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

@Component
@RequiredArgsConstructor
public class FriendRequestMapper {
    private final UserRepository userRepository;
    public FriendRequestDto toFriendRequestDto(FriendRequest friendRequest){
        User sender = userRepository.findByUsername(friendRequest.getSender().getUsername());
        User receiver = userRepository.findByUsername(friendRequest.getSender().getUsername());
        return FriendRequestDto.builder()
                .sender(sender)
                .receiver(receiver)
                .status(friendRequest.getStatus())
                .build();
    }
}
