package com.waggle.global.response.swagger;

import com.waggle.domain.application.dto.ApplicationResponse;
import com.waggle.global.response.SuccessResponse;

public class ApplicationSuccessResponse extends SuccessResponse<ApplicationResponse> {

    public ApplicationSuccessResponse(int code, String message, ApplicationResponse payload) {
        super(code, message, payload);
    }
}
