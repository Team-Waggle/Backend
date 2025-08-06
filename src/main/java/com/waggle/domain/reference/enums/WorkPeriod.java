package com.waggle.domain.reference.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum WorkPeriod {
    SHORT_TERM("단기"),
    MEDIUM_TERM("중기"),
    LONG_TERM("장기"),
    UNDECIDED("미정");

    @JsonProperty("display_name")
    private final String displayName;
}
