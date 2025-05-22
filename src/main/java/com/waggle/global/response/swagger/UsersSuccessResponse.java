package com.waggle.global.response.swagger;

import com.waggle.domain.user.dto.UserResponseDto;
import com.waggle.global.response.SuccessResponse;
import java.util.Set;

public class UsersSuccessResponse extends SuccessResponse<Set<UserResponseDto>> {

    public UsersSuccessResponse(int code, String message, Set<UserResponseDto> payload) {
        super(code, message, payload);
    }
}
