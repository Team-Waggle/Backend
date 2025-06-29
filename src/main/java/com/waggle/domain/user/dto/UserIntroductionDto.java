package com.waggle.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.waggle.domain.reference.enums.CollaborationStyle;
import com.waggle.domain.reference.enums.CommunicationStyle;
import com.waggle.domain.reference.enums.IntroductionType;
import com.waggle.domain.reference.enums.Mbti;
import com.waggle.domain.reference.enums.ProblemSolvingApproach;
import com.waggle.domain.reference.enums.WorkStyle;
import com.waggle.domain.user.entity.UserIntroduction;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record UserIntroductionDto(
    @JsonProperty("communication_styles")
    @Schema(description = "소통 스타일", example = "[\"DYNAMIC\", \"LOGICAL_PERSUASIVE\"]")
    List<CommunicationStyle> communicationStyles,

    @JsonProperty("collaboration_styles")
    @Schema(description = "협업 스타일", example = "[\"LEADERSHIP\", \"TEAM_FOCUSED\"]")
    List<CollaborationStyle> collaborationStyles,

    @JsonProperty("work_styles")
    @Schema(description = "일하는 방식", example = "[\"GOAL_ORIENTED\", \"DETAIL_ORIENTED\"]")
    List<WorkStyle> workStyles,

    @JsonProperty("problem_solving_approaches")
    @Schema(description = "문제 해결 방식", example = "[\"DATA_DRIVEN\", \"CREATIVE\"]")
    List<ProblemSolvingApproach> problemSolvingApproaches,

    @JsonProperty("mbti")
    @Schema(description = "MBTI 유형", example = "ENFP")
    Mbti mbti
) {

    public static UserIntroductionDto from(List<UserIntroduction> introductions) {
        List<CommunicationStyle> communicationStyles = introductions.stream()
            .filter(intro -> intro.getIntroductionType() == IntroductionType.COMMUNICATION_STYLE)
            .map(intro -> CommunicationStyle.valueOf(intro.getSubIntroduction()))
            .toList();

        List<CollaborationStyle> collaborationStyles = introductions.stream()
            .filter(intro -> intro.getIntroductionType() == IntroductionType.COLLABORATION_STYLE)
            .map(intro -> CollaborationStyle.valueOf(intro.getSubIntroduction()))
            .toList();

        List<WorkStyle> workStyles = introductions.stream()
            .filter(intro -> intro.getIntroductionType() == IntroductionType.WORK_STYLE)
            .map(intro -> WorkStyle.valueOf(intro.getSubIntroduction()))
            .toList();

        List<ProblemSolvingApproach> problemSolvingApproaches = introductions.stream()
            .filter(
                intro -> intro.getIntroductionType() == IntroductionType.PROBLEM_SOLVING_APPROACH)
            .map(intro -> ProblemSolvingApproach.valueOf(intro.getSubIntroduction()))
            .toList();

        Mbti mbti = introductions.stream()
            .filter(intro -> intro.getIntroductionType() == IntroductionType.MBTI)
            .findFirst()
            .map(intro -> Mbti.valueOf(intro.getSubIntroduction()))
            .orElse(null);

        return new UserIntroductionDto(
            communicationStyles,
            collaborationStyles,
            workStyles,
            problemSolvingApproaches,
            mbti
        );
    }
}
