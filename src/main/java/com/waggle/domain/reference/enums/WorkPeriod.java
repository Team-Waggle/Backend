package com.waggle.domain.reference.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WorkPeriod {
    ONE_MONTH("1개월"),
    TWO_MONTHS("2개월"),
    THREE_MONTHS("3개월"),
    FOUR_MONTHS("4개월"),
    FIVE_MONTHS("5개월"),
    SIX_MONTHS("6개월"),
    OVER_SIX_MONTHS("6개월 이상");

    private final String description;
}
