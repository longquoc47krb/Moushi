package id.longquoc.messenger.config.websocket;

import com.sun.security.auth.UserPrincipal;
import id.longquoc.messenger.service.AuthService;
import id.longquoc.messenger.service.chat.MessageSender;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomChannelInterceptor implements ChannelInterceptor {
    private final AuthService authService;
    private final MessageSender devMessageSender;

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        var command = accessor != null ? accessor.getCommand() : null;
        if (command != null && command.equals(StompCommand.CONNECT)) {
            String  authHeader = accessor.getFirstNativeHeader("Authorization");
            String  apiKey = accessor.getFirstNativeHeader("x-api-key");
            if (authHeader == null) devMessageSender.sendError(accessor, "Missing Authorization Header (Bearer)");
            if (apiKey == null) devMessageSender.sendError(accessor, "Missing api key");
            try {
                Authentication authenticatedUser = authService.authenticateUserFromHeaderAuth(authHeader);
                if (accessor.getUser() == null) accessor.setUser(new UserPrincipal(authenticatedUser.getName()));

            } catch (ExpiredJwtException | MalformedJwtException e) {
                log.warn(e.getMessage());
                devMessageSender.sendError(accessor, e.getMessage());
            }
        }

        return message;
    }

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        ChannelInterceptor.super.postSend(message, channel, sent);
    }

    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        log.debug("<- Incoming <- " + accessor.getMessageType() + " <-");
    }

    @Override
    public boolean preReceive(MessageChannel channel) {
        return ChannelInterceptor.super.preReceive(channel);
    }

    @Override
    public Message<?> postReceive(Message<?> message, MessageChannel channel) {
        return ChannelInterceptor.super.postReceive(message, channel);
    }

    @Override
    public void afterReceiveCompletion(Message<?> message, MessageChannel channel, Exception ex) {
        ChannelInterceptor.super.afterReceiveCompletion(message, channel, ex);
    }
}
