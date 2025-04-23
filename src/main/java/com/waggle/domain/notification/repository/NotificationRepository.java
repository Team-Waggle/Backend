package com.waggle.domain.notification.repository;

import com.waggle.domain.notification.entity.Notification;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    int countByUserIdAndIsReadFalse(UUID userId);

    List<Notification> findByUserIdOrderByCreatedAtDesc(UUID userId);

    @Modifying
    int updateIsReadByUserIdAndIsReadFalse(UUID userId, boolean isRead);
}
