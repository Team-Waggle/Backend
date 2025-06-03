package com.waggle.domain.notification.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record NotificationsResponseDto(
    @Schema(description = "알림 목록")
    List<NotificationResponseDto> notificationResponseDtos,

    @Schema(description = "다음 페이지 존재 여부")
    boolean hasNext,

    @Schema(description = "다음 페이지 커서")
    Long nextCursor
) {

    public static NotificationsResponseDto of(
        List<NotificationResponseDto> notificationResponseDtos,
        boolean hasNext,
        Long nextCursor
    ) {
        return new NotificationsResponseDto(notificationResponseDtos, hasNext, nextCursor);
    }
}
