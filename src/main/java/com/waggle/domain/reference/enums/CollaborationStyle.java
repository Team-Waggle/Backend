package com.waggle.domain.reference.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CollaborationStyle {
    LEADERSHIP("리더십 스타일"),
    SUPPORTER("서포터형"),
    FREE_SPIRIT("자유로운 영혼"),
    FEEDBACKER("피드백커"),
    CREATIVE_THINKER("기획 아이디어형"),
    MOTIVATED("추진력 넘치는"),
    DATA_DRIVEN("데이터 중심적"),
    TEAMWORK_FOCUSED("팀워크 중시형"),
    DETAIL_ORIENTED("디테일 집착형"),
    FLEXIBLE_SOLVER("유연한 문제 해결자");

    @JsonProperty("display_name")
    private final String displayName;
}
