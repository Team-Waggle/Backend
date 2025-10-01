package com.waggle.domain.user.repository;

import com.waggle.domain.user.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmail(String email);

    Optional<User> findByProviderId(String providerId);

    List<User> findByEmailIn(List<String> emails);

    @Query("""
        SELECT u FROM User u
        WHERE u.name LIKE :query% OR u.email LIKE :query%
        """)
    List<User> searchByNameOrEmail(@Param("query") String query);
}
