package com.waggle.domain.notification.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.waggle.domain.notification.entity.Notification;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

@Schema(description = "알림 응답 DTO")
public record NotificationResponse(
    @Schema(description = "알림 ID", example = "550e8400-e29b-41d4-a716-446655440000")
    @JsonProperty("id")
    Long id,

    @Schema(description = "알림 제목", example = "새로운 프로젝트 신청이 있습니다")
    @JsonProperty("title")
    String title,

    @Schema(description = "알림 내용", example = "홍길동님이 'Waggle 백엔드 개발' 프로젝트에 참여 신청을 했습니다.")
    @JsonProperty("content")
    String content,

    @Schema(description = "리다이렉트 URL", example = "/waggle")
    @JsonProperty("redirect_url")
    String redirectUrl,

    @Schema(description = "읽음 여부", example = "false")
    @JsonProperty("is_read")
    boolean isRead,

    @Schema(description = "생성일자", example = "2025-07-27T10:30:00")
    @JsonProperty("created_at")
    Instant createdAt,

    @Schema(description = "수정일자", example = "2025-07-27T10:30:00")
    @JsonProperty("updated_at")
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
