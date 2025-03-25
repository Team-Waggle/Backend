package com.waggle.domain.reference.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WorkWay {
    ONLINE("온라인"),
    OFFLINE("오프라인"),
    ONLINE_OFFLINE("온/오프라인");

    private final String name;
}
