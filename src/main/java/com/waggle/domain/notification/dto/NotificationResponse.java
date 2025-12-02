package com.waggle.domain.notification.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.waggle.domain.notification.entity.Notification;
import com.waggle.domain.project.SimpleProjectInfo;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "알림 응답 DTO")
public record NotificationResponse(
    @Schema(description = "알림 ID", example = "550e8400-e29b-41d4-a716-446655440000")
    @JsonProperty("id")
    Long id,

    @Schema(description = "프로젝트 정보")
    @JsonProperty("project")
    SimpleProjectInfo project,

    @Schema(description = "리다이렉트 URL", example = "/waggle")
    @JsonProperty("redirect_url")
    String redirectUrl,

    @Schema(description = "읽음 여부", example = "false")
    @JsonProperty("is_read")
    boolean isRead,

    @Schema(description = "생성일자", example = "2025-07-27T10:30:00")
    @JsonProperty("created_at")
    LocalDateTime createdAt,

    @Schema(description = "수정일자", example = "2025-07-27T10:30:00")
    @JsonProperty("updated_at")
    LocalDateTime updatedAt
) {

    public static NotificationResponse of(Notification notification, SimpleProjectInfo project) {
        return new NotificationResponse(
            notification.getId(),
            project,
            notification.getRedirectUrl(),
            notification.isRead(),
            notification.getCreatedAt(),
            notification.getUpdatedAt()
        );
    }
}
