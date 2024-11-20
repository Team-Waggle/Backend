package com.waggle.global.exception;

import com.waggle.global.response.code.JwtTokenErrorResponseCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class JwtTokenException extends RuntimeException {
    private final JwtTokenErrorResponseCode jwtTokenErrorStatus;

    @Override
    public String getMessage() {
        return jwtTokenErrorStatus.getMessage();
    }
}
