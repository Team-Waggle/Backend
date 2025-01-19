package com.waggle.global.response.swagger;

import com.waggle.domain.reference.entity.Industry;
import com.waggle.global.response.SuccessResponse;

public class IndustriesSuccessResponse extends SuccessResponse<Industry[]> {
    public IndustriesSuccessResponse(int code, String message, Industry[] payload) {
        super(code, message, payload);
    }
}
