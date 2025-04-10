package com.waggle.domain.reference.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WorkTime {
    MORNING("오전"),
    AFTERNOON("오후"),
    EVENING("저녁"),
    NIGHT("심야"),
    DAWN("새벽");

    private final String displayName;
}
