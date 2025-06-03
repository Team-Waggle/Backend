package com.waggle.domain.notification.dto;

import com.waggle.domain.notification.entity.Notification;
import java.time.LocalDateTime;

public record NotificationResponseDto(
    Long id,
    String title,
    String content,
    String redirectUrl,
    boolean isRead,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public static NotificationResponseDto from(Notification notification) {
        return new NotificationResponseDto(
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
