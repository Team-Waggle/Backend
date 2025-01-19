package com.waggle.global.response.swagger;

import com.waggle.domain.reference.entity.MainIntroduce;
import com.waggle.global.response.SuccessResponse;

public class MainIntroducesSuccessResponse extends SuccessResponse<MainIntroduce[]> {
    public MainIntroducesSuccessResponse(int code, String message, MainIntroduce[] payload) {
        super(code, message, payload);
    }
}
