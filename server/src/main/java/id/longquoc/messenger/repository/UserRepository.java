package id.longquoc.messenger.repository;

import id.longquoc.messenger.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
 Boolean existsByEmail(String email);
 Boolean existsByUsername(String username);

 @Query("""
         FROM User u
         WHERE u.username = :username
         """)
 User findByUsername(String username);
 @Query("""
         FROM User u
         WHERE u.email = :email
         """)
 User findByEmail(String email);
 @Query("""
         FROM User u
         WHERE u.username = :username OR u.email = :email
         """)
 User findByEmailOrUsername(String email, String username);

 @Query("""
         FROM User u
         WHERE u.username = :username AND u.password = :password
         """)
 User findByUsernameAndPassword(String username, String password);
 Optional<User> findById(UUID userId);

 @Query("FROM User u " +
         "WHERE u.lastOnline " +
         "IS NULL")
 List<User> getOnlineUsersExceptThis();
 @Modifying
 @Query("UPDATE User u " +
         "SET u.lastOnline = :lastOnline, u.sessions = u.sessions + :counter " +
         "WHERE u.username = :username")
 void setOnline(@Param("username") String username, @Param("lastOnline") Instant lastOnline, @Param("counter") byte counter );
 @Query("SELECT u.sessions " +
         "FROM User u " +
         "WHERE u.username = :username")
 Byte getSessions(String username);
}
