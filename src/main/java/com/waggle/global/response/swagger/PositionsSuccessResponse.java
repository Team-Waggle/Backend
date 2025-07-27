package com.waggle.global.response.swagger;

import com.waggle.domain.reference.enums.Position;
import com.waggle.global.response.SuccessResponse;

public class PositionsSuccessResponse extends SuccessResponse<Position[]> {

    public PositionsSuccessResponse(int code, String message, Position[] payload) {
        super(code, message, payload);
    }
}
