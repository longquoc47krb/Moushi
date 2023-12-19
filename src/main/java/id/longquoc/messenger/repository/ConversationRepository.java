package id.longquoc.messenger.repository;

import id.longquoc.messenger.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ConversationRepository extends JpaRepository<Conversation, UUID> {
    @Query(value = """
            SELECT u.conversationsByUserId from User u \s
            WHERE u.id = :userId
            """)
    List<Conversation> findAllByUserId(UUID userId);
    @Query(value = """
            SELECT u.conversationsByUserId from User u \s
            WHERE u.username = :username
            """)
    List<Conversation> findAllByUsername(String username);

}
