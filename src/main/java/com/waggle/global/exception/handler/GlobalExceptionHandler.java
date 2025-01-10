package com.waggle.global.exception.handler;

import com.waggle.global.exception.JwtTokenException;
import com.waggle.global.response.ApiStatus;
import com.waggle.global.response.BaseResponse;
import com.waggle.global.response.ErrorResponse;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JwtTokenException.class)
    public ResponseEntity<BaseResponse<String>> handleJwtTokenException(JwtTokenException ex) {
        return ErrorResponse.of(ex.getStatus());
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<BaseResponse<String>> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex) {
        return ErrorResponse.of(ApiStatus._NOT_FOUND);
    }
}
