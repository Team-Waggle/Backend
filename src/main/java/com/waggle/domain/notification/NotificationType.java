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
    APPLICATION_REJECTED(
        "지원 거절",
        "'%s' 프로젝트 지원이 거절되었습니다. 다른 기회에 도전해보세요."
    );

    @Getter
    private final String title;
    private final String contentTemplate;

    public String getContent(Object... args) {
        return contentTemplate.formatted(args);
    }
}
