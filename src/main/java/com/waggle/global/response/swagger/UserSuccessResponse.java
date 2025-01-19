package com.waggle.global.response.swagger;

import com.waggle.domain.user.entity.User;
import com.waggle.global.response.SuccessResponse;

public class UserSuccessResponse extends SuccessResponse<User> {
    public UserSuccessResponse(int code, String message, User payload) {
        super(code, message, payload);
    }
}
