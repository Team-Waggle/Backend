package com.waggle.domain.reference.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JobRole {
    FRONTEND("프론트엔드"),
    BACKEND("백엔드"),
    DESIGNER("디자이너"),
    IOS("iOS"),
    ANDROID("안드로이드"),
    DEVOPS("데브옵스"),
    PLANNER("기획자"),
    MARKETER("마케터");

    private final String name;
}
