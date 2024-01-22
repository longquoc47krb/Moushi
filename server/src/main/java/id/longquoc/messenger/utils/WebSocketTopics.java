package id.longquoc.messenger.utils;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class WebSocketUtil {

    //Chat topics
    public static final String GET_DETAIL_CONVERSATION = "/topic/conversations/{conversationId}";
    public static final String SEND_PRIVATE_MESSAGE = "/topic/conversations/send-message";

    // Friend Request topics
    public static final String SEND_FRIEND_REQUEST = "/topic/friend-requests/{receiverId}";
}
