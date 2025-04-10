package com.waggle.domain.reference.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum WorkTime {
    MORNING("오전"),
    AFTERNOON("오후"),
    EVENING("저녁"),
    NIGHT("심야"),
    DAWN("새벽");

    @JsonProperty("display_name")
    private final String displayName;
}
