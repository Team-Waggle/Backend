package com.waggle.domain.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.waggle.domain.application.ApplicationStatus;
import com.waggle.domain.project.entity.ProjectApplicant;
import com.waggle.domain.reference.enums.Position;
import com.waggle.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

public record ProjectAppliedUserResponseDto(
    @Schema(description = "사용자 ID", example = "550e8400-e29b-41d4-a716-446655440000")
    @JsonProperty("id")
    UUID id,

    @Schema(description = "프로필 이미지 URL")
    @JsonProperty("profile_img_url")
    String profileImageUrl,

    @Schema(description = "사용자 이름", example = "홍길동")
    @JsonProperty("name")
    String name,

    @Schema(description = "사용자 이메일", example = "hong@gmail.com")
    @JsonProperty("email")
    String email,

    @Schema(
        description = "사용자 직무",
        example = "BACKEND"
    )
    @JsonProperty("position")
    Position position,

    @Schema(
        description = "사용자 경력",
        example = "3"
    )
    @JsonProperty("year_count")
    int yearCount,

    @JsonProperty("project_applicant_id")
    UUID projectApplicantId,

    @JsonProperty("status")
    ApplicationStatus status
) {

    public static ProjectAppliedUserResponseDto of(User user, ProjectApplicant projectApplicant) {
        return new ProjectAppliedUserResponseDto(
            user.getId(),
            user.getProfileImageUrl(),
            user.getName(),
            user.getEmail(),
            user.getPosition(),
            user.getYearCount(),
            projectApplicant.getId(),
            projectApplicant.getStatus()
        );
    }
}
