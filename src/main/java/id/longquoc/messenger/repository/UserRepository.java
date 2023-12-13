package id.longquoc.messenger.repository;

import id.longquoc.messenger.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
 Boolean existsByEmail(String email);
 User findByEmail(String email);
 User findByEmailOrUsername(String email, String username);
}
