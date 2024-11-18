package com.waggle.domain.auth.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.waggle.domain.auth.entity.RefreshToken;

import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    @Query("SELECT u FROM RefreshToken u WHERE u.user.id = :userId")
    RefreshToken findByUserId(UUID userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM RefreshToken u WHERE u.user.id = :userId")
    void deleteByUserId(UUID userId);
}
