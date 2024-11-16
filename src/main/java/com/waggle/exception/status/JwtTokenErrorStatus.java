package com.waggle.exception.status;

import com.waggle.global.dto.ResponseDto;
import com.waggle.global.interfaces.Status;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum JwtTokenErrorStatus implements Status {
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, 401, "유효하지 않은 토큰입니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, 401, "유효하지 않은 액세스 토큰입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, 401, "유효하지 않은 리프레쉬 토큰입니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "해당 유저 ID의 리프레쉬 토큰이 없습니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    public ResponseDto getReason() {
        return ResponseDto.builder()
                .isSuccess(false)
                .code(code)
                .message(message)
                .build();
    }

    public ResponseDto getReasonHttpStatus() {
        return ResponseDto.builder()
                .isSuccess(false)
                .httpStatus(httpStatus)
                .code(code)
                .message(message)
                .build();
    }
}
