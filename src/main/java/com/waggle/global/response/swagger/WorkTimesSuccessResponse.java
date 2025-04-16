package com.waggle.global.response.swagger;

import com.waggle.domain.reference.enums.WorkTime;
import com.waggle.global.response.SuccessResponse;

public class WorkTimesSuccessResponse extends SuccessResponse<WorkTime[]> {

    public WorkTimesSuccessResponse(int code, String message, WorkTime[] payload) {
        super(code, message, payload);
    }
}
