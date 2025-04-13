package com.waggle.global.response.swagger;

import com.waggle.domain.reference.enums.WorkWay;
import com.waggle.global.response.SuccessResponse;

public class WorkWaysSuccessResponse extends SuccessResponse<WorkWay[]> {

    public WorkWaysSuccessResponse(int code, String message, WorkWay[] payload) {
        super(code, message, payload);
    }
}
