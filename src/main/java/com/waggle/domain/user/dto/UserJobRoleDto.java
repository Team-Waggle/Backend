package com.waggle.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.waggle.domain.reference.enums.JobRole;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserJobRoleDto(

    @JsonProperty("job_role")
    @Schema(description = "직무", example = "BACKEND")
    JobRole jobRole,

    @JsonProperty("year_cnt")
    @Schema(description = "경력", example = "3")
    int yearCnt
) {

}
