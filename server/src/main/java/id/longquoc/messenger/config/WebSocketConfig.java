package id.longquoc.messenger.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.longquoc.messenger.config.websocket.CustomChannelInterceptor;
import io.micrometer.common.lang.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

@Configuration
@Slf4j
@RequiredArgsConstructor
@EnableScheduling
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final CustomChannelInterceptor channelInterceptor;

    private TaskScheduler taskScheduler;
    @Autowired
    public void setTaskExecutor(@Lazy TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue")
                .setHeartbeatValue(new long[] {20000, 15000})
                .setTaskScheduler(taskScheduler);
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-chat").setAllowedOrigins("*").withSockJS();
    }

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
}
