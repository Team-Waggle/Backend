package com.waggle.domain.notification.repository;

import com.waggle.domain.notification.entity.Notification;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    int countByUserIdAndIsReadFalse(UUID userId);

    @Query("SELECT n.createdAt FROM Notification n WHERE n.id = :id")
    Optional<Instant> findCreatedAtById(@Param("id") Long notificationId);

    @Query("""
        SELECT n FROM Notification n
        WHERE n.user.id = :userId
        AND (:cursor IS NULL OR
             (n.createdAt < :cursorCreatedAt OR
              (n.createdAt = :cursorCreatedAt AND n.id < :cursor)))
        ORDER BY n.createdAt DESC, n.id DESC
        """)
    List<Notification> findByUserIdWithCursor(
        @Param("userId") UUID userId,
        @Param("cursor") Long cursor,
        @Param("cursorCreatedAt") Instant cursorCreatedAt,
        Pageable pageable
    );

    @Modifying
    @Query("UPDATE Notification n SET n.isRead = :isRead WHERE n.user.id = :userId AND n.isRead = false")
    int updateIsReadByUserIdAndIsReadFalse(
        @Param("userId") UUID userId,
        @Param("isRead") boolean isRead
    );
}
