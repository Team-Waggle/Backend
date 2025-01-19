package com.waggle.global.response.swagger;

import com.waggle.domain.reference.entity.Job;
import com.waggle.global.response.SuccessResponse;

public class JobsSuccessResponse extends SuccessResponse<Job[]> {
    public JobsSuccessResponse(int code, String message, Job[] payload) {
        super(code, message, payload);
    }
}
