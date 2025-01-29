package com.waggle.global.exception;

import com.waggle.global.response.ApiStatus;
import lombok.Getter;

@Getter
public class S3Exception extends RuntimeException {
    private final ApiStatus status;

    public S3Exception(ApiStatus status) {
        super(status.getMessage());
        this.status = status;
    }

    public S3Exception(ApiStatus status, Throwable cause) {
        super(status.getMessage(), cause);
        this.status = status;
    }
}
