package com.waggle.global.response.swagger;

import com.waggle.global.response.SuccessResponse;

public class EmptySuccessResponse extends SuccessResponse<Void> {

    public EmptySuccessResponse(int code, String message, Void payload) {
        super(code, message, payload);
    }
}
