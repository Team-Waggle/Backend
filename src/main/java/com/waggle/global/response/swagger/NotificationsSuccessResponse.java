package com.waggle.global.response.swagger;

import com.waggle.domain.notification.dto.NotificationResponseDto;
import com.waggle.global.response.SuccessResponse;
import java.util.Set;

public class NotificationsSuccessResponse extends SuccessResponse<Set<NotificationResponseDto>> {

    public NotificationsSuccessResponse(
        int code,
        String message,
        Set<NotificationResponseDto> payload
    ) {
        super(code, message, payload);
    }
}
