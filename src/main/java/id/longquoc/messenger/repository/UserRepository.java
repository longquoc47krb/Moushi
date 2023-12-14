package id.longquoc.messenger.repository;

import id.longquoc.messenger.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
 Boolean existsByEmail(String email);
 Boolean existsByUsername(String username);
 User findByEmail(String email);
 User findByEmailOrUsername(String email, String username);
 Optional<User> findById(UUID userId);
}
