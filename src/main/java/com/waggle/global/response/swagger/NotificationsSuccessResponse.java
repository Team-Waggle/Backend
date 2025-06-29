package com.waggle.global.response.swagger;

import com.waggle.domain.notification.dto.NotificationResponse;
import com.waggle.global.response.SuccessResponse;
import java.util.List;

public class NotificationsSuccessResponse extends SuccessResponse<List<NotificationResponse>> {

    public NotificationsSuccessResponse(
        int code,
        String message,
        List<NotificationResponse> payload
    ) {
        super(code, message, payload);
    }
}
