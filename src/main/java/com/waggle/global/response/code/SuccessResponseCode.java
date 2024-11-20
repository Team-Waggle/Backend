package com.waggle.global.response.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import com.waggle.global.response.ApiResponseMessage;

@Getter
@AllArgsConstructor
public enum SuccessResponseCode implements ResponseCode {
    // 전역 응답 코드
    _OK(HttpStatus.OK, 200, "성공입니다."),
    _CREATED(HttpStatus.CREATED, 201, "생성에 성공했습니다."),
    _ACCEPTED(HttpStatus.ACCEPTED, 202, "요청이 수락되었습니다."),

    // 커스텀 응답 코드
    _CREATED_ACCESS_TOKEN(HttpStatus.CREATED, 201, "액세스 토큰 재발행에 성공했습니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    @Override
    public ApiResponseMessage getReason() {
        return ApiResponseMessage.builder()
                .isSuccess(true)
                .code(code)
                .message(message)
                .build();
    }

    @Override
    public ApiResponseMessage getReasonHttpStatus() {
        return ApiResponseMessage.builder()
                .isSuccess(true)
                .httpStatus(httpStatus)
                .code(code)
                .message(message)
                .build();
    }
}