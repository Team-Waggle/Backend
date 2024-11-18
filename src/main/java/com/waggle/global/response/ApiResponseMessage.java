package com.waggle.global.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class ApiResponseMessage {
    private HttpStatus httpStatus;
    private final boolean isSuccess;
    private final int code;
    private final String message;
}