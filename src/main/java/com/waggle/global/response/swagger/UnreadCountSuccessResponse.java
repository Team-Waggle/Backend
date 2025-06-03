package com.waggle.global.response.swagger;

import com.waggle.global.response.SuccessResponse;

public class UnreadCountSuccessResponse extends SuccessResponse<Integer> {

    public UnreadCountSuccessResponse(int code, String message, Integer payload) {
        super(code, message, payload);
    }
}
