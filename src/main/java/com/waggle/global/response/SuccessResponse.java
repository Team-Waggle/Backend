package com.waggle.global.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.ResponseEntity;

@Schema(description = "성공 응답 엔티티")
public class SuccessResponse<T> extends BaseResponse<T> {

    @Schema(description = "성공 여부", example = "true")
    protected final boolean isSuccess = true;

    @Schema(description = "응답 코드", example = "200")
    protected final int code;

    @Schema(description = "응답 메시지", example = "성공적으로 처리되었습니다.")
    protected final String message;

    @Schema(description = "응답 데이터", example = "{'id': 1, 'name': '홍길동'}")
    protected final T payload;

    public SuccessResponse(int code, String message, T payload) {
        super(true, code, message, payload);
        this.code = code;
        this.message = message;
        this.payload = payload;
    }

    public static <T> ResponseEntity<BaseResponse<T>> of(ApiStatus code, T data) {
        BaseResponse<T> response = new SuccessResponse<>(code.getCode(), code.getMessage(), data);
        return ResponseEntity.status(code.getHttpStatus()).body(response);
    }
}
