package com.waggle.domain.reference.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum IntroductionType {
    COMMUNICATION_STYLE("소통 스타일", CommunicationStyle.class),
    COLLABORATION_STYLE("협업 성향", CollaborationStyle.class),
    WORK_STYLE("작업 방식", WorkStyle.class),
    PROBLEM_SOLVING_APPROACH("문제 해결 방식", ProblemSolvingApproach.class),
    MBTI("MBTI", Mbti.class);

    private final String displayName;
    private final Class<? extends Enum<?>> enumClass;
}
