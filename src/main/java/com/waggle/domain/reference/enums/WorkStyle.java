package com.waggle.domain.reference.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum WorkStyle {
    GOAL_ORIENTED("목표 지향적"),
    PUNCTUAL("시간 엄수형"),
    FLEXIBLE_WORKING("유연 근무 가능형"),
    MULTI_TASKER("멀티태스킹 능력자"),
    FOCUSED("집중 몰입형"),
    THOROUGH_PLANNER("철저한 계획형"),
    IMPROVISER("즉흥적으로 해결형"),
    DETAIL_CHECKER("꼼꼼한 점검형"),
    DEADLINE_LOVER("마감 압박을 즐기는"),
    CREATIVE_EXPERIMENTER("창의적인 실험가");

    @JsonProperty("display_name")
    private final String displayName;
}
