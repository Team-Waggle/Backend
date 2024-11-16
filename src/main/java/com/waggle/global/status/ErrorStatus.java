package com.waggle.global.status;

import com.waggle.global.dto.ResponseDto;
import com.waggle.global.interfaces.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements Status {
    // 전역 응답 코드
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, 400, "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, 401, "인증에 실패했습니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, 403, "접근 권한이 없습니다."),
    _NOT_FOUND(HttpStatus.NOT_FOUND, 404, "찾을 수 없습니다."),
    _METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, 405, "허용되지 않은 메소드입니다."),
    _CONFLICT(HttpStatus.CONFLICT, 409, "충돌이 발생했습니다."),
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, "서버 내부 오류가 발생했습니다."),
    _SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, 503, "서비스를 사용할 수 없습니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    @Override
    public ResponseDto getReason() {
        return ResponseDto.builder()
                .isSuccess(false)
                .code(code)
                .message(message)
                .build();
    }

    @Override
    public ResponseDto getReasonHttpStatus() {
        return ResponseDto.builder()
                .isSuccess(false)
                .httpStatus(httpStatus)
                .code(code)
                .message(message)
                .build();
    }
}
