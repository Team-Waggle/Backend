package com.waggle.domain.notification.dto;

import com.waggle.domain.notification.NotificationType;

public record NotificationRequestDto(
    NotificationType type,
    String redirectUrl,
    Object[] contentArgs
) {

    public static NotificationRequestDto of(
        NotificationType type,
        String redirectUrl,
        Object... contentArgs
    ) {
        return new NotificationRequestDto(type, redirectUrl, contentArgs);
    }
}
