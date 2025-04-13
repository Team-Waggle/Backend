package com.waggle.global.response.swagger;

import com.waggle.domain.reference.enums.IntroductionType;
import com.waggle.global.response.SuccessResponse;

public class IntroductionsTypesSuccessResponse extends SuccessResponse<IntroductionType[]> {

    public IntroductionsTypesSuccessResponse(int code, String message, IntroductionType[] payload) {
        super(code, message, payload);
    }
}
