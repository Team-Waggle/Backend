package com.waggle.domain.reference.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CommunicationStyle {
    CLEAR_AND_CONCISE("명쾌하게 한마디로"),
    OBSERVANT("눈치 보는 스타일"),
    RELAXED("느긋한 편"),
    DYNAMIC("다이나믹 소통"),
    HUMOROUS("유머러스한 스타일"),
    SERIOUS_AND_DEEP("진지하게 깊이 있는"),
    FAST_AND_SIMPLE("빠르고 간결하게"),
    CALM_EXPLAINER("차분하게 설명형"),
    EMPATHY_FOCUSED("공감 중심의 소통"),
    LOGICAL_PERSUADER("논리적으로 설득형");

    @JsonProperty("display_name")
    private final String displayName;
}
