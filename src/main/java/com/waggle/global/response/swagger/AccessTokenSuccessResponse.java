package com.waggle.global.response.swagger;

import com.waggle.domain.auth.dto.AccessTokenVo;
import com.waggle.global.response.SuccessResponse;

public class AccessTokenSuccessResponse extends SuccessResponse<AccessTokenVo> {

    public AccessTokenSuccessResponse(int code, String message, AccessTokenVo payload) {
        super(code, message, payload);
    }
}
