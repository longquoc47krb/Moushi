package id.longquoc.messenger.service.interfaces;

import id.longquoc.messenger.model.ChatMessage;
import id.longquoc.messenger.payload.request.MessageReq;

import java.util.UUID;

public interface IChatMessageService {
    ChatMessage createMessage(MessageReq messageReq);
    void removeMessageById(UUID id);
}
