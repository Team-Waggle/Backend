package com.waggle.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.waggle.global.response.code.ResponseCode;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

@Getter
@RequiredArgsConstructor
@Schema(description = "API 응답 엔티티")
public class ApiResponseEntity<T> {
    @Schema(description = "성공 여부", example = "true")
    @JsonProperty("isSuccess")
    private final Boolean isSuccess;

    @Schema(description = "응답 코드", example = "200")
    private final int code;

    @Schema(description = "응답 메시지", example = "요청이 성공적으로 처리되었습니다.")
    private final String message;

    @Schema(description = "응답 데이터", example = "{\"name\": \"John\", \"age\": 30}")
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