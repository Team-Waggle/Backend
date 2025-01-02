package com.waggle.global.exception.handler;

import com.waggle.global.exception.JwtTokenException;
import com.waggle.global.response.BaseResponse;
import com.waggle.global.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JwtTokenException.class)
    public ResponseEntity<BaseResponse<String>> handleJwtTokenException(JwtTokenException ex) {
        return ErrorResponse.of(ex.getStatus());
    }
}
