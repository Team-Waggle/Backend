package com.waggle.global.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class ResponseDto {
    private HttpStatus httpStatus;
    private final boolean isSuccess;
    private final int code;
    private final String message;
}