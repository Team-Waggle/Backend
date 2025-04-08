package com.waggle.domain.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.waggle.domain.project.entity.ProjectRecruitment;
import com.waggle.domain.reference.enums.JobRole;
import io.swagger.v3.oas.annotations.media.Schema;

public record ProjectRecruitmentDto(
    @Schema(description = "직무", example = "JAVA")
    @JsonProperty("job_role")
    JobRole jobRole,

    @Schema(description = "모집 인원 수", example = "2")
    @JsonProperty("remaining_count")
    int remainingCount,

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
