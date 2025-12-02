package com.waggle.domain.notification.dto;

import com.waggle.domain.notification.NotificationType;

public record CreateNotificationRequest(
    NotificationType type,
    Long projectId,
    String redirectUrl
) {

    public static CreateNotificationRequest of(
        NotificationType type,
        Long projectId,
        String redirectUrl
    ) {
        return new CreateNotificationRequest(type, projectId, redirectUrl);
    }
}
