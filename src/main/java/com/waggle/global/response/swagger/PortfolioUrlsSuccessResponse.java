package com.waggle.global.response.swagger;

import com.waggle.domain.reference.entity.PortfolioUrl;
import com.waggle.global.response.SuccessResponse;

public class PortfolioUrlsSuccessResponse extends SuccessResponse<PortfolioUrl[]> {
    public PortfolioUrlsSuccessResponse(int code, String message, PortfolioUrl[] payload) {
        super(code, message, payload);
    }
}
