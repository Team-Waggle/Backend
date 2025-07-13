package com.waggle.domain.application.dto;

import com.waggle.domain.application.Application;
import com.waggle.domain.application.ApplicationStatus;
import com.waggle.domain.projectV2.dto.ProjectResponse;
import com.waggle.domain.reference.enums.Position;
import com.waggle.domain.user.dto.UserResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

@Schema(description = "지원 응답 DTO")
public record ApplicationResponse(
    @Schema(description = "지원 ID", example = "42")
    Long id,

    @Schema(description = "지원 직무", example = "BACKEND")
    Position position,

    @Schema(description = "지원 상태", example = "PENDING")
    ApplicationStatus status,

    @Schema(description = "지원자")
    UserResponseDto user,

    @Schema(description = "지원 프로젝트")
    ProjectResponse project,

    @Schema(description = "지원 시간", example = "2025-06-08T10:30:00Z")
    Instant createdAt,

    @Schema(description = "지원 상태 변경 시간", example = "2025-06-08T10:35:00Z")
    Instant updatedAt
) {

    public static ApplicationResponse from(Application application) {
        return new ApplicationResponse(
            application.getId(),
            application.getPosition(),
            application.getStatus(),
            null,
            ProjectResponse.from(application.getProject()),
            application.getCreatedAt(),
            application.getUpdatedAt()
        );
    }
}
