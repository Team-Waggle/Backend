package com.waggle.domain.reference.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProblemSolvingApproach {
    PROACTIVE("적극적인 돌파형"),
    STRATEGIC_ANALYST("분석하고 전략적 해결형"),
    CREATIVE_INNOVATOR("창의적이고 독창적"),
    COLLABORATIVE("협력해서 해결형"),
    DATA_DRIVEN_APPROACH("데이터 기반으로 접근"),
    DECISIVE("신속하고 결단력 있는"),
    PATIENT("차분히 기다리는 스타일"),
    EXPERIMENT_AND_REVISE("실험하고 수정 반복형"),
    INTUITIVE("직관에 의존하는 스타일"),
    OPINION_FOCUSED("의견 조율을 중시하는");

    private final String description;
}
