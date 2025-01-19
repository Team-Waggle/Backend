package com.waggle.global.response.swagger;

import com.waggle.domain.reference.entity.SubIntroduce;
import com.waggle.global.response.SuccessResponse;

public class SubIntroducesSuccessResponse extends SuccessResponse<SubIntroduce[]> {
    public SubIntroducesSuccessResponse(int code, String message, SubIntroduce[] payload) {
        super(code, message, payload);
    }
}
