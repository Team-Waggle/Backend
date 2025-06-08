package com.waggle.global.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.ResponseEntity;

@Schema(description = "에러 응답 엔티티")
public class ErrorResponse<T> extends BaseResponse<Void> {

    public ErrorResponse(int code, String message) {
        super(false, code, message, null);
    }

    public static ResponseEntity<BaseResponse<Void>> of(ApiStatus code) {
        BaseResponse<Void> response = new ErrorResponse<>(code.getCode(), code.getMessage());
        return ResponseEntity.status(code.getHttpStatus()).body(response);
    }

    public static ResponseEntity<BaseResponse<Void>> of(ApiStatus code, String message) {
        BaseResponse<Void> response = new ErrorResponse<>(code.getCode(), message);
        return ResponseEntity.status(code.getHttpStatus()).body(response);
    }
}
