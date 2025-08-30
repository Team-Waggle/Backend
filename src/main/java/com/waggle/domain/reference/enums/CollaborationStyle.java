package com.waggle.domain.reference.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CollaborationStyle {
    LEADERSHIP,
    SUPPORTER,
    FREE_SPIRIT,
    FEEDBACKER,
    CREATIVE_THINKER,
    MOTIVATED,
    DATA_DRIVEN,
    TEAMWORK_FOCUSED,
    DETAIL_ORIENTED,
    FLEXIBLE_SOLVER
}
