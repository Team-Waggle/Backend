package com.waggle.domain.notification.dto;

import com.waggle.domain.notification.NotificationType;

public record CreateNotificationRequest(
    NotificationType type,
    String redirectUrl,
    Object[] contentArgs
) {

    public static CreateNotificationRequest of(
        NotificationType type,
        String redirectUrl,
        Object... contentArgs
    ) {
        return new CreateNotificationRequest(type, redirectUrl, contentArgs);
    }
}
