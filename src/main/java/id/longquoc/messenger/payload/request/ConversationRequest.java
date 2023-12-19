package id.longquoc.messenger.payload.request;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationRequest {
    @Nullable
    private String name;
    private List<UUID> participants;

}
