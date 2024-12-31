package com.waggle.global.exception;

import com.waggle.global.response.ResponseStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class JwtTokenException extends RuntimeException {
    private final ResponseStatus errorStatus;

    @Override
    public String getMessage() {
        return errorStatus.getMessage();
    }
}
