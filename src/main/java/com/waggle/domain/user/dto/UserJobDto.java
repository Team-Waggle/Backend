package com.waggle.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class UserJobDto {

    @JsonProperty("job_id")
    private Long jobId;

    @JsonProperty("year_cnt")
    private int yearCnt;
}
