package com.waggle.global.response.swagger;

import com.waggle.domain.member.dto.MemberResponse;
import com.waggle.global.response.SuccessResponse;

public class MemberSuccessResponse extends SuccessResponse<MemberResponse> {

    public MemberSuccessResponse(int code, String message, MemberResponse payload) {
        super(code, message, payload);
    }
}
