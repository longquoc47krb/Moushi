package id.longquoc.messenger.repository;

import id.longquoc.messenger.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<ChatMessage, UUID> {

    @Query(value = """
            SELECT id, sender, content
            FROM ChatMessage m
            WHERE m.conversation = :conversationId
            """)
    List<ChatMessage> findMessagesByConversationId(String conversationId);

}
