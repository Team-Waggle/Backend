package com.waggle.global.response.swagger;

import com.waggle.domain.reference.enums.JobRole;
import com.waggle.global.response.SuccessResponse;

public class JobRolesSuccessResponse extends SuccessResponse<JobRole[]> {

    public JobRolesSuccessResponse(int code, String message, JobRole[] payload) {
        super(code, message, payload);
    }
}
