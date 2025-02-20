package com.waggle.domain.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
public class ProjectJobInputDto {

    @JsonProperty("job_id")
    @Schema(description = "직무 고유키", example = "1")
    private Long jobId;

    @JsonProperty("cnt")
    @Schema(description = "인원", example = "3")
    private int cnt;
}
