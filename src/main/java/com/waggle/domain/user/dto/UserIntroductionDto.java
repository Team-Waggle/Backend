package com.waggle.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.waggle.domain.reference.enums.CollaborationStyle;
import com.waggle.domain.reference.enums.CommunicationStyle;
import com.waggle.domain.reference.enums.Mbti;
import com.waggle.domain.reference.enums.ProblemSolvingApproach;
import com.waggle.domain.reference.enums.WorkStyle;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;

public record UserIntroductionDto(
    @JsonProperty("communication_styles")
    @Schema(description = "소통 스타일", example = "[\"DYNAMIC\", \"LOGICAL_PERSUASIVE\"]")
    Set<CommunicationStyle> communicationStyles,

    @JsonProperty("collaboration_styles")
    @Schema(description = "협업 스타일", example = "[\"LEADERSHIP\", \"TEAM_FOCUSED\"]")
    Set<CollaborationStyle> collaborationStyles,

    @JsonProperty("work_styles")
    @Schema(description = "일하는 방식", example = "[\"GOAL_ORIENTED\", \"DETAIL_ORIENTED\"]")
    Set<WorkStyle> workStyles,

    @JsonProperty("problem_solving_approaches")
    @Schema(description = "문제 해결 방식", example = "[\"DATA_DRIVEN\", \"CREATIVE\"]")
    Set<ProblemSolvingApproach> problemSolvingApproaches,

    @JsonProperty("mbti")
    @Schema(description = "MBTI 유형", example = "ENFP")
    Mbti mbti
) {

}
