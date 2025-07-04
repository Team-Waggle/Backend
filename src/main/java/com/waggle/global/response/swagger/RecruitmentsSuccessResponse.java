package com.waggle.global.response.swagger;

import com.waggle.domain.recruitment.dto.RecruitmentResponse;
import com.waggle.global.response.SuccessResponse;
import java.util.List;

public class RecruitmentsSuccessResponse extends SuccessResponse<List<RecruitmentResponse>> {

    public RecruitmentsSuccessResponse(
        int code,
        String message,
        List<RecruitmentResponse> payload
    ) {
        super(code, message, payload);
    }
}
