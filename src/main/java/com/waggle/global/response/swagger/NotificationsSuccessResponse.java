package com.waggle.global.response.swagger;

import com.waggle.domain.notification.dto.NotificationResponseDto;
import com.waggle.global.response.SuccessResponse;
import java.util.List;

public class NotificationsSuccessResponse extends SuccessResponse<List<NotificationResponseDto>> {

    public NotificationsSuccessResponse(
        int code,
        String message,
        List<NotificationResponseDto> payload
    ) {
        super(code, message, payload);
    }
}
