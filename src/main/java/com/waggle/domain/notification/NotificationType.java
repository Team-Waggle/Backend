package com.waggle.domain.notification;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum NotificationType {
    APPLICATION_RECEIVED(
        "지원 접수",
        "'%s' 님이 '%s' 프로젝트에 지원했습니다."
    ),
    APPLICATION_ACCEPTED(
        "지원 승인",
        "'%s' 프로젝트 지원이 승인되었습니다. 팀에 참여해 주세요!"
    ),
    APPLICATION_CONFIRMED(
        "모집 성공",
        "'%s' 님이 '%s' 프로젝트에 합류했습니다."
    ),
    APPLICATION_REJECTED(
        "지원 거절",
        "'%s' 프로젝트 지원이 거절되었습니다. 다른 기회에 도전해보세요."
    ),
    DEADLINE_APPROACHING(
        "마감 임박",
        "'%s' 프로젝트 모집이 %s일 후에 마감됩니다. 서둘러 지원해보세요!"
    ),
    DEADLINE_CLOSED(
        "모집 마감",
        "오늘 '%s' 프로젝트 모집이 마감됩니다."
    );

    @Getter
    private final String title;
    private final String contentTemplate;

    public String getContent(Object... args) {
        return contentTemplate.formatted(args);
    }
}
