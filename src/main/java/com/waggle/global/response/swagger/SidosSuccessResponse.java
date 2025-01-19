package com.waggle.global.response.swagger;

import com.waggle.domain.reference.entity.Sido;
import com.waggle.global.response.SuccessResponse;

public class SidosSuccessResponse extends SuccessResponse<Sido[]> {
    public SidosSuccessResponse(int code, String message, Sido[] payload) {
        super(code, message, payload);
    }
}
