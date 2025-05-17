package com.waggle.domain.notification.repository;

import com.waggle.domain.notification.entity.Notification;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    int countByUserIdAndIsReadFalse(UUID userId);

    List<Notification> findByUserIdOrderByCreatedAtDesc(UUID userId);

    @Modifying
    @Query("UPDATE Notification n SET n.isRead = :isRead WHERE n.user.id = :userId AND n.isRead = false")
    int updateIsReadByUserIdAndIsReadFalse(
        @Param("userId") UUID userId,
        @Param("isRead") boolean isRead
    );
}
