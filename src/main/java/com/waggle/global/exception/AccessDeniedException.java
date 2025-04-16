package com.waggle.global.exception;

import com.waggle.global.response.ApiStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AccessDeniedException extends RuntimeException {

    private final ApiStatus status;
}
