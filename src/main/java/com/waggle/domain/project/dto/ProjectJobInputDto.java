package com.waggle.domain.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.waggle.domain.reference.enums.JobRole;
import io.swagger.v3.oas.annotations.media.Schema;

public record ProjectJobInputDto(

    @JsonProperty("job_role")
    @Schema(description = "직무", example = "BACKEND")
    JobRole jobRole,

    @JsonProperty("count")
    @Schema(description = "인원", example = "3")
    int count
) {

}
