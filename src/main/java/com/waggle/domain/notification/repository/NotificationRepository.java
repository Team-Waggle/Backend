package com.waggle.domain.notification.repository;

import com.waggle.domain.notification.entity.Notification;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

}
