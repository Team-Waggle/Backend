package com.waggle.global.response.swagger;

import com.waggle.domain.application.dto.ApplicationResponse;
import com.waggle.global.response.SuccessResponse;
import java.util.List;

public class ApplicationsSuccessResponse extends SuccessResponse<List<ApplicationResponse>> {

    public ApplicationsSuccessResponse(
        int code,
        String message,
        List<ApplicationResponse> payload
    ) {
        super(code, message, payload);
    }
}
