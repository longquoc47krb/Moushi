package id.longquoc.messenger.repository;

import id.longquoc.messenger.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    @Query(value = """
            SELECT id, sender, receiver, content
            FROM Message m
            WHERE m.conversation = :conversationId
            """)
    List<Message> findMessagesByConversationId(String conversationId);

}
