package com.waggle.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserJobDto(

    @JsonProperty("job_id")
    @Schema(description = "직무 고유키", example = "1")
    Long jobId,

    @JsonProperty("year_cnt")
    @Schema(description = "경력 년수", example = "3")
    int yearCnt
) {

}
