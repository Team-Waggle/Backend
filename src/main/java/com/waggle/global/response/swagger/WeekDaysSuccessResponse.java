package com.waggle.global.response.swagger;

import com.waggle.domain.reference.entity.WeekDays;
import com.waggle.global.response.SuccessResponse;

public class WeekDaysSuccessResponse extends SuccessResponse<WeekDays[]> {
    public WeekDaysSuccessResponse(int code, String message, WeekDays[] payload) {
        super(code, message, payload);
    }
}
