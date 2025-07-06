package com.waggle.global.response.swagger;

import com.waggle.domain.user.dto.UserResponseDto;
import com.waggle.global.response.SuccessResponse;
import java.util.List;

public class UsersSuccessResponse extends SuccessResponse<List<UserResponseDto>> {

    public UsersSuccessResponse(int code, String message, List<UserResponseDto> payload) {
        super(code, message, payload);
    }
}
