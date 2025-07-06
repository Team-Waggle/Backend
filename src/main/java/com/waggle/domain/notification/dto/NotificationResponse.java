package com.waggle.domain.notification.dto;

import com.waggle.domain.notification.entity.Notification;
import java.time.Instant;

public record NotificationResponse(
    Long id,
    String title,
    String content,
    String redirectUrl,
    boolean isRead,
    Instant createdAt,
    Instant updatedAt
) {

    public static NotificationResponse from(Notification notification) {
        return new NotificationResponse(
            notification.getId(),
            notification.getTitle(),
            notification.getContent(),
            notification.getRedirectUrl(),
            notification.isRead(),
            notification.getCreatedAt(),
            notification.getUpdatedAt()
        );
    }
}
