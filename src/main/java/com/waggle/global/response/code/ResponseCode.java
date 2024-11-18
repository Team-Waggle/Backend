package com.waggle.global.response.code;

import com.waggle.global.response.ApiResponseMessage;

public interface ResponseCode {
    public ApiResponseMessage getReason();
    public ApiResponseMessage getReasonHttpStatus();
}
