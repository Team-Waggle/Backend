package com.waggle.domain.reference.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum IntroductionType {
    COMMUNICATION_STYLE("소통 스타일", CommunicationStyle.class),
    COLLABORATION_STYLE("협업 성향", CollaborationStyle.class),
    WORK_STYLE("작업 방식", WorkStyle.class),
    PROBLEM_SOLVING_APPROACH("문제 해결 방식", ProblemSolvingApproach.class),
    MBTI("MBTI", Mbti.class);

    @JsonProperty("display_name")
    private final String displayName;

    @JsonIgnore
    private final Class<? extends Enum<?>> enumClass;
}
