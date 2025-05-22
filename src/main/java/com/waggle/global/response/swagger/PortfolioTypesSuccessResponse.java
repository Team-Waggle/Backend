package com.waggle.global.response.swagger;

import com.waggle.domain.reference.enums.PortfolioType;
import com.waggle.global.response.SuccessResponse;

public class PortfolioTypesSuccessResponse extends SuccessResponse<PortfolioType[]> {

    public PortfolioTypesSuccessResponse(int code, String message, PortfolioType[] payload) {
        super(code, message, payload);
    }
}
