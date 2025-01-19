package com.waggle.global.response.swagger;

import com.waggle.domain.reference.entity.DurationOfWorking;
import com.waggle.global.response.SuccessResponse;

public class DurationOfWorkingsSuccessResponse extends SuccessResponse<DurationOfWorking[]> {
    public DurationOfWorkingsSuccessResponse(int code, String message, DurationOfWorking[] payload) {
        super(code, message, payload);
    }
}
