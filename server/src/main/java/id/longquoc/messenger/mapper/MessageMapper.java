package id.longquoc.messenger.mapper;

import id.longquoc.messenger.dto.chat.ConversationDto;
import id.longquoc.messenger.dto.chat.MessageDto;
import id.longquoc.messenger.dto.user.OnlineUserDto;
import id.longquoc.messenger.enums.TypingState;
import id.longquoc.messenger.model.Conversation;
import id.longquoc.messenger.model.ChatMessage;
import id.longquoc.messenger.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MessageMapper {
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    @Mapping(source = "sender.username", target = "sender")
    @Mapping(source = "conversation.id", target = "conversationId")
    MessageDto toDto(ChatMessage message);
    default ConversationDto toConversationDto(Conversation conversation, String currentUser) {
        List<User> participants = conversation.getParticipants();
        for(User user : participants) {
            if (user.getUsername().equals(currentUser)) {
                return ConversationDto.builder()
                        .id(conversation.getId())
                        .fullName(user.getFullName())
                        .username(user.getUsername())
                        .image(user.getProfilePicture())
                        .lastOnline(user.getLastOnline())
                        .typingState(TypingState.IDLE)
                        .build();
            }
        }
        return null;
    }
    default String toUsername(Conversation conversation, String username) {
        List<User> participants = conversation.getParticipants();
        for(User user : participants) {
            if (user.getUsername().equals(username)) {
                return user.getUsername();
            }
        }
        return username;
    }
    default OnlineUserDto toDto(User user) {
        return OnlineUserDto.builder()
                .username(user.getUsername())
                .fullName(user.getFullName())
                .profilePicture(user.getProfilePicture())
                .build();
    }


}
