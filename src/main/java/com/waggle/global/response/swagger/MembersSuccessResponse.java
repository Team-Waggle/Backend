package com.waggle.global.response.swagger;

import com.waggle.domain.member.dto.MemberResponse;
import com.waggle.global.response.SuccessResponse;
import java.util.List;

public class MembersSuccessResponse extends SuccessResponse<List<MemberResponse>> {

    public MembersSuccessResponse(int code, String message, List<MemberResponse> payload) {
        super(code, message, payload);
    }
}
