package com.waggle.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserJobDto {

    @JsonProperty("job_id")
    private Long jobId;

    @JsonProperty("year_cnt")
    private String yearCnt;
}
