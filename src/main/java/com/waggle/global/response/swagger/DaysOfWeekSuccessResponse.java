package com.waggle.global.response.swagger;

import com.waggle.global.response.SuccessResponse;
import java.time.DayOfWeek;

public class DaysOfWeekSuccessResponse extends SuccessResponse<DayOfWeek[]> {

    public DaysOfWeekSuccessResponse(int code, String message, DayOfWeek[] payload) {
        super(code, message, payload);
    }
}
