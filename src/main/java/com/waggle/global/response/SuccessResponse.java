package com.waggle.global.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.ResponseEntity;

@Schema(description = "성공 응답 DTO")
public class SuccessResponse<T> extends BaseResponse<T> {

    public SuccessResponse(int code, String message, T payload) {
        super(true, code, message, payload);
    }

    public static <T> ResponseEntity<BaseResponse<T>> of(ApiStatus code, T data) {
        BaseResponse<T> response = new SuccessResponse<>(code.getCode(), code.getMessage(), data);
        return ResponseEntity.status(code.getHttpStatus()).body(response);
    }
}
