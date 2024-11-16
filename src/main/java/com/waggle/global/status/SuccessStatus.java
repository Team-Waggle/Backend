package com.waggle.global.status;

import com.waggle.global.dto.ResponseDto;
import com.waggle.global.interfaces.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements Status {
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
    public ResponseDto getReason() {
        return ResponseDto.builder()
                .isSuccess(true)
                .code(code)
                .message(message)
                .build();
    }

    @Override
    public ResponseDto getReasonHttpStatus() {
        return ResponseDto.builder()
                .isSuccess(true)
                .httpStatus(httpStatus)
                .code(code)
                .message(message)
                .build();
    }
}