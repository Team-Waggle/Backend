package com.waggle.global.response.swagger;

import com.waggle.domain.user.dto.UserResponseDto;
import com.waggle.global.response.SuccessResponse;

public class UserDtoSuccessResponse extends SuccessResponse<UserResponseDto> {

    public UserDtoSuccessResponse(int code, String message, UserResponseDto payload) {
        super(code, message, payload);
    }
}
