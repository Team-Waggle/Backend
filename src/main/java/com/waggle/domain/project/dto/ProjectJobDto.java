package com.waggle.domain.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ProjectJobDto {

    @JsonProperty("job_id")
    @Schema(description = "직무 고유키", example = "1")
    private Long jobId;

    @JsonProperty("recruitment_cnt")
    @Schema(description = "모집 인원", example = "3")
    private int recruitmentCnt;
}
