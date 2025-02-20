package com.waggle.global.response.swagger;

import com.waggle.domain.user.dto.UserWithProjectResponseDto;
import com.waggle.domain.user.entity.User;
import com.waggle.global.response.SuccessResponse;

public class UserSuccessResponse extends SuccessResponse<UserWithProjectResponseDto> {
    public UserSuccessResponse(int code, String message, UserWithProjectResponseDto payload) {
        super(code, message, payload);
    }
}
