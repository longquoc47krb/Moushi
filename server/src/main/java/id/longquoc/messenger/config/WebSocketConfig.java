package id.longquoc.messenger.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.longquoc.messenger.config.websocket.CustomChannelInterceptor;
import id.longquoc.messenger.config.websocket.SocketHandler;
import io.micrometer.common.lang.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.converter.DefaultContentTypeResolver;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;

import java.util.List;

@Configuration
@Slf4j
@RequiredArgsConstructor
@EnableWebSocket
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer, WebSocketConfigurer {
    private final CustomChannelInterceptor channelInterceptor;
    /*TODO:It declares a CustomChannelInterceptor field to add a custom filter for WebSocket message channels. */

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }
    /*TODO: register a WebSocket endpoint named /ws-chat,
       allowing any origin, and using SockJS to support browsers
       that do not support WebSocket.*/
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-chat").setAllowedOrigins("*").withSockJS();
    }
    /*TODO: Add a message converter using Jackson to convert Java objects
       to JSON and vice versa.*/
    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        DefaultContentTypeResolver contentTypeResolver = new DefaultContentTypeResolver();
        contentTypeResolver.setDefaultMimeType(MimeTypeUtils.APPLICATION_JSON);
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(new ObjectMapper());
        converter.setContentTypeResolver(contentTypeResolver);
        messageConverters.add(converter);

        return WebSocketMessageBrokerConfigurer.super.configureMessageConverters(messageConverters);
    }
    /*TODO: It overrides the configureClientInboundChannel and configureClientOutboundChannel methods
       to add filters for incoming and outgoing message channels.
        This code uses StompHeaderAccessor to retrieve message types and log them.*/
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration
                .interceptors(channelInterceptor)
                .interceptors(new ChannelInterceptor() {
                    @Override
                    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
                        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
                        log.debug("<- Incoming <- " + accessor.getMessageType() + " <-");
                    }
                });
    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration
                .interceptors(new ChannelInterceptor() {
                    @Override
                    public void afterSendCompletion(@NonNull Message<?> message,
                                                    @NonNull MessageChannel channel,
                                                    boolean sent,
                                                    @Nullable Exception ex) {
                        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
                        log.debug("-> Outgoing -> " + accessor.getMessageType() + " ->");
                    }
                });
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myHandler(), "/data");
    }
    @Bean
    public WebSocketHandler myHandler() {
        return new SocketHandler();
    }
}
