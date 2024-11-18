package com.waggle.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.waggle.global.response.code.ResponseCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

@Getter
@RequiredArgsConstructor
public class ApiResponseEntity<T> {
    @JsonProperty("isSuccess")
    private final Boolean isSuccess;
    private final int code;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T payload;
    public static <T> ResponseEntity<ApiResponseEntity<T>> onSuccess(ResponseCode code, T data) {
        ApiResponseEntity<T> response = new ApiResponseEntity<>(true, code.getReasonHttpStatus().getCode(), code.getReasonHttpStatus().getMessage(), data);
        return ResponseEntity.status(code.getReasonHttpStatus().getHttpStatus()).body(response);
    }

    public static <T> ResponseEntity<ApiResponseEntity<T>> onFailure(ResponseCode code) {
        ApiResponseEntity<T> response = new ApiResponseEntity<>(false, code.getReasonHttpStatus().getCode(), code.getReasonHttpStatus().getMessage(), null);
        return ResponseEntity.status(code.getReasonHttpStatus().getHttpStatus()).body(response);
    }
}