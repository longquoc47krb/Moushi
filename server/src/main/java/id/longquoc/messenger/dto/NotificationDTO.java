package id.longquoc.messenger.dto;

import id.longquoc.messenger.enums.NotificationType;
import lombok.Data;

@Data
public class NotificationDTO {
    private String content;
    private NotificationType type;
    private String metadata;
}
