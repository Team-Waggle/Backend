package com.waggle.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ApiStatus {
    // 성공
    _OK(HttpStatus.OK, 200, "성공입니다."),
    _CREATED(HttpStatus.CREATED, 201, "생성에 성공했습니다."),
    _ACCEPTED(HttpStatus.ACCEPTED, 202, "요청이 수락되었습니다."),
    _NO_CONTENT(HttpStatus.NO_CONTENT, 204, "No Content"),
        // 커스텀
    _REISSUE_ACCESS_TOKEN(HttpStatus.CREATED, 201, "액세스 토큰 재발행에 성공했습니다."),
    _CREATE_ACCESS_TOKEN(HttpStatus.CREATED, 201, "액세스 토큰 발행에 성공했습니다."),

    // 실패
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, 400, "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, 401, "인증에 실패했습니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, 403, "접근 권한이 없습니다."),
    _NOT_FOUND(HttpStatus.NOT_FOUND, 404, "찾을 수 없습니다."),
    _METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, 405, "허용되지 않은 메소드입니다."),
    _CONFLICT(HttpStatus.CONFLICT, 409, "충돌이 발생했습니다."),
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, "서버 내부 오류가 발생했습니다."),
    _SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, 503, "서비스를 사용할 수 없습니다."),
        // 커스텀
    _INVALID_TOKEN(HttpStatus.UNAUTHORIZED, 401, "유효하지 않은 토큰입니다."),
    _INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, 401, "유효하지 않은 액세스 토큰입니다."),
    _INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, 401, "유효하지 않은 리프레쉬 토큰입니다."),
    _INVALID_TEMPORARY_TOKEN(HttpStatus.UNAUTHORIZED, 401, "유효하지 않은 임시 토큰입니다."),
    _REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "해당 유저 ID의 리프레쉬 토큰이 없습니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;
}
