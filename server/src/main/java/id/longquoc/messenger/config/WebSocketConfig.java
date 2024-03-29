package id.longquoc.messenger.config;

import id.longquoc.messenger.config.websocket.CustomChannelInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@Slf4j
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final CustomChannelInterceptor channelInterceptor;
    /*TODO:It declares a CustomChannelInterceptor field to add a custom filter for WebSocket message channels. */

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }
    /*TODO: register a WebSocket endpoint named /ws,
       allowing any origin, and using SockJS to support browsers
       that do not support WebSocket.*/
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOrigins("http://localhost:3000").withSockJS();
    }
    /*TODO: Add a message converter using Jackson to convert Java objects
       to JSON and vice versa.*/
//    @Override
//    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
//        DefaultContentTypeResolver contentTypeResolver = new DefaultContentTypeResolver();
//        contentTypeResolver.setDefaultMimeType(MimeTypeUtils.APPLICATION_JSON);
//        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
//        converter.setObjectMapper(new ObjectMapper());
//        converter.setContentTypeResolver(contentTypeResolver);
//        messageConverters.add(converter);
//
//        return WebSocketMessageBrokerConfigurer.super.configureMessageConverters(messageConverters);
//    }
    /*TODO: It overrides the configureClientInboundChannel and configureClientOutboundChannel methods
       to add filters for incoming and outgoing message channels.
        This code uses StompHeaderAccessor to retrieve message types and log them.*/
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration
                .interceptors(channelInterceptor);
    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration
                .interceptors(channelInterceptor);
    }

}
