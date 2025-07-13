package com.waggle.domain.projectV2.dto;

import com.waggle.domain.projectV2.ProjectV2;
import com.waggle.domain.user.dto.UserResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "프로젝트 응답 DTO")
public record ProjectResponse(
    @Schema(description = "프로젝트 ID", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID id,

    @Schema(description = "프로젝트 이름", example = "waggle")
    String name,

    @Schema(description = "프로젝트 태그라인", example = "프로젝트 팀원 모집 웹 애플리케이션")
    String tagline,

    @Schema(description = "프로젝트 설명")
    String description,

    @Schema(description = "프로젝트 순번 ID (정렬용)", example = "1")
    Long sequenceId,

    @Schema(description = "프로젝트 리더 정보")
    UserResponseDto leader
) {

    public static ProjectResponse from(ProjectV2 project) {
        return new ProjectResponse(
            project.getId(),
            project.getName(),
            project.getTagline(),
            project.getDescription(),
            project.getSequenceId(),
            null
        );
    }
}
