package com.waggle.domain.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.waggle.domain.project.entity.ProjectRecruitment;
import com.waggle.domain.reference.enums.JobRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ProjectRecruitmentDto(
    @NotNull(message = "직무는 필수 입력 항목입니다")
    @Schema(description = "직무", example = "JAVA")
    @JsonProperty("job_role")
    JobRole jobRole,

    @Min(value = 0, message = "모집 인원 수는 0 이상이어야 합니다")
    @Max(value = 10, message = "모집 인원 수는 10 이하여야 합니다")
    @Schema(description = "모집 인원 수", example = "2")
    @JsonProperty("remaining_count")
    int remainingCount,

    @PositiveOrZero(message = "현재 인원 수는 0 이상이어야 합니다")
    @Schema(description = "현재 인원 수", example = "3")
    @JsonProperty("current_count")
    int currentCount
) {

    public static ProjectRecruitmentDto from(ProjectRecruitment recruitment) {
        return new ProjectRecruitmentDto(
            recruitment.getJobRole(),
            recruitment.getRemainingCount(),
            recruitment.getCurrentCount()
        );
    }
}
