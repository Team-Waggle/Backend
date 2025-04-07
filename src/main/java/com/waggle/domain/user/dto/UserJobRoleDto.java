package com.waggle.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.waggle.domain.reference.enums.JobRole;
import com.waggle.domain.user.entity.UserJobRole;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserJobRoleDto(

    @JsonProperty("job_role")
    @Schema(description = "직무", example = "BACKEND")
    JobRole jobRole,

    @JsonProperty("year_count")
    @Schema(description = "경력", example = "3")
    int yearCount
) {

    public static UserJobRoleDto from(UserJobRole userJobRole) {
        return new UserJobRoleDto(userJobRole.getJobRole(), userJobRole.getYearCount());
    }
}
