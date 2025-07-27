package com.waggle.global.response.swagger;

import com.waggle.domain.user.dto.UserResponse;
import com.waggle.global.response.SuccessResponse;

public class UserSuccessResponse extends SuccessResponse<UserResponse> {

    public UserSuccessResponse(int code, String message, UserResponse payload) {
        super(code, message, payload);
    }
}
