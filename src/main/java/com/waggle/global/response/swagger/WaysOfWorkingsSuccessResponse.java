package com.waggle.global.response.swagger;

import com.waggle.domain.reference.entity.WaysOfWorking;
import com.waggle.global.response.SuccessResponse;

public class WaysOfWorkingsSuccessResponse extends SuccessResponse<WaysOfWorking[]> {
    public WaysOfWorkingsSuccessResponse(int code, String message, WaysOfWorking[] payload) {
        super(code, message, payload);
    }
}
