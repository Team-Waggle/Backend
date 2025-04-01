package com.waggle.domain.user.repository;

import com.waggle.domain.user.entity.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @Query("SELECT u FROM User u WHERE u.id = :userId")
    Optional<User> findByUserId(UUID userId);

    User findByProviderId(String providerId);
}
