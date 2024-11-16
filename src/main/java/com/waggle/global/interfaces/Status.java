package com.waggle.global.interfaces;

import com.waggle.global.dto.ResponseDto;

public interface Status {
    public ResponseDto getReason();
    public ResponseDto getReasonHttpStatus();
}
