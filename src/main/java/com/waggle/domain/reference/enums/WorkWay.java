package com.waggle.domain.reference.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum WorkWay {
    ONLINE("온라인만"),
    OFFLINE("오프라인만"),
    ONLINE_OFFLINE("모두 가능");

    @JsonProperty("display_name")
    private final String displayName;
}
