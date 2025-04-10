package com.waggle.domain.reference.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Mbti {
    ISTJ("ISTJ", "청렴결백한 논리주의자"),
    ISFJ("ISFJ", "용감한 수호자"),
    INFJ("INFJ", "통찰력 있는 예언자"),
    INTJ("INTJ", "전략적인 사색가"),
    ISTP("ISTP", "호기심 많은 해결사"),
    ISFP("ISFP", "호기심 많은 예술가"),
    INFP("INFP", "열정적인 중재자"),
    INTP("INTP", "논리적인 사색가"),
    ESTP("ESTP", "모험을 즐기는 활동가"),
    ESFP("ESFP", "자유로운 영혼의 연예인"),
    ENFP("ENFP", "열정적인 활동가"),
    ENTP("ENTP", "논쟁을 즐기는 발명가"),
    ESTJ("ESTJ", "효율적인 관리자"),
    ESFJ("ESFJ", "사교적인 협력가"),
    ENFJ("ENFJ", "따뜻한 카리스마 리더"),
    ENTJ("ENTJ", "결단력 있는 통솔자");

    private final String code;
    private final String displayName;
}
