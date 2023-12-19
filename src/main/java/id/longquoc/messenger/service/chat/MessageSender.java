package id.longquoc.messenger.service.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.Nullable;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class MessageSender {

    private MessageChannel clientOutboundChannel;
    @Autowired
    public void setClientOutboundChannel(@Lazy MessageChannel clientOutboundChannel) {
        this.clientOutboundChannel = clientOutboundChannel;
    }
    public void sendError(StompHeaderAccessor accessor, @Nullable String message) {

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.create(StompCommand.ERROR);
        headerAccessor.setSessionId(accessor.getSessionId());
        if (message != null) headerAccessor.setMessage(message);

        this.clientOutboundChannel.send(MessageBuilder.createMessage(new byte[0],
                headerAccessor.getMessageHeaders()));
    }
    public void sendReceipt(StompHeaderAccessor accessor, @Nullable String receipt) {

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.create(StompCommand.RECEIPT);
        headerAccessor.setSessionId(accessor.getSessionId());
        headerAccessor.setDestination(accessor.getDestination());
        headerAccessor.setReceiptId(accessor.getReceiptId());
        if (receipt != null) headerAccessor.setReceipt(receipt);

        // TODO: remove it just for logging
        System.out.println(headerAccessor.getMessageHeaders());

        this.clientOutboundChannel.send(MessageBuilder.createMessage(new byte[0],
                headerAccessor.getMessageHeaders()));
    }

}
