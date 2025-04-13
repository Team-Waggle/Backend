package com.waggle.global.response.swagger;

import com.waggle.domain.reference.enums.WorkPeriod;
import com.waggle.global.response.SuccessResponse;

public class WorkPeriodSuccessResponse extends SuccessResponse<WorkPeriod[]> {

    public WorkPeriodSuccessResponse(int code, String message, WorkPeriod[] payload) {
        super(code, message, payload);
    }
}
