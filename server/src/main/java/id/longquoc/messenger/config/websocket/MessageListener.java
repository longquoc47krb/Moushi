 /*
package id.longquoc.messenger.config.websocket;

import id.longquoc.messenger.common.Constants;
import id.longquoc.messenger.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.lang.constant.Constable;

@Component
public class MessageListener {
    @Autowired
    SimpMessagingTemplate template;

    @KafkaListener(
            topics = Constants.KAFKA_TOPIC,
            groupId = Constants.GROUP_ID
    )
    public void listen(Message message) {
        System.out.println("sending via kafka listener..");
        template.convertAndSend("/topic/group", message);
    }
}
*/
