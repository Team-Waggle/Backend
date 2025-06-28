package com.waggle.global.exception.handler;

import com.waggle.global.exception.AccessDeniedException;
import com.waggle.global.exception.JwtTokenException;
import com.waggle.global.exception.ProjectException;
import com.waggle.global.exception.S3Exception;
import com.waggle.global.response.ApiStatus;
import com.waggle.global.response.BaseResponse;
import com.waggle.global.response.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JwtTokenException.class)
    public ResponseEntity<BaseResponse<Void>> handleJwtTokenException(JwtTokenException e) {
        return ErrorResponse.of(e.getStatus());
    }

    @ExceptionHandler(S3Exception.class)
    public ResponseEntity<BaseResponse<Void>> handleS3Exception(S3Exception e) {
        return ErrorResponse.of(e.getStatus(), e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<BaseResponse<Void>> handleAccessDeniedException(
        AccessDeniedException e
    ) {
        return ErrorResponse.of(e.getStatus());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<BaseResponse<Void>> handleEntityNotFoundException(
        EntityNotFoundException e
    ) {
        return ErrorResponse.of(ApiStatus._NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<BaseResponse<Void>> handleHttpMessageNotReadableException(
        HttpMessageNotReadableException e
    ) {
        return ErrorResponse.of(ApiStatus._BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(ProjectException.class)
    public ResponseEntity<BaseResponse<Void>> handleProjectException(ProjectException e) {
        return ErrorResponse.of(e.getStatus(), e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BaseResponse<Void>> handleIllegalArgumentException(
        IllegalArgumentException e
    ) {
        return ErrorResponse.of(ApiStatus._BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<BaseResponse<Void>> handleIllegalStateException(
        IllegalStateException e
    ) {
        return ErrorResponse.of(ApiStatus._CONFLICT, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Void>> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException e
    ) {
        StringBuilder errorMessage = new StringBuilder();

        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errorMessage.append(fieldName).append(": ").append(message).append("; ");
        });

        return ErrorResponse.of(ApiStatus._BAD_REQUEST, errorMessage.toString());
    }
}
