package com.waggle.apis.auth.entity;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    @Query("SELECT u FROM RefreshToken u WHERE u.userId = :userId")
    RefreshToken findByUserId(UUID userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM RefreshToken u WHERE u.userId = :userId")
    void deleteByUserId(UUID userId);
}
