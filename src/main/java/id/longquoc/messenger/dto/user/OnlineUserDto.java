package id.longquoc.messenger.dto.user;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnlineUserDto {
    private String username;
    private String fullName;
    private byte[] profilePicture;
}
