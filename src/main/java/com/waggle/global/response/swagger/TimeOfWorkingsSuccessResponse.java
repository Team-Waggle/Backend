package com.waggle.global.response.swagger;

import com.waggle.domain.reference.entity.TimeOfWorking;
import com.waggle.global.response.SuccessResponse;

public class TimeOfWorkingsSuccessResponse extends SuccessResponse<TimeOfWorking[]> {
    public TimeOfWorkingsSuccessResponse(int code, String message, TimeOfWorking[] payload) {
        super(code, message, payload);
    }
}
