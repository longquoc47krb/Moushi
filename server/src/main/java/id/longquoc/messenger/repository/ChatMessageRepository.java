package id.longquoc.messenger.repository;

import id.longquoc.messenger.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {

    @Query("SELECT m FROM ChatMessage m WHERE m.conversation.id = :conversationId")
    List<ChatMessage> findMessagesByConversationId(UUID conversationId);

}
