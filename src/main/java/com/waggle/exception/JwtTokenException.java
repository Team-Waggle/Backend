package com.waggle.exception;

import com.waggle.exception.status.JwtTokenErrorStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class JwtTokenException extends RuntimeException {
    private final JwtTokenErrorStatus jwtTokenErrorStatus;

    @Override
    public String getMessage() {
        return jwtTokenErrorStatus.getMessage();
    }
}
