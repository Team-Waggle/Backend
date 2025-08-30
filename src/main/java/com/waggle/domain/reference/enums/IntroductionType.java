package com.waggle.domain.reference.enums;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum IntroductionType {
    COMMUNICATION_STYLE(CommunicationStyle.class),
    COLLABORATION_STYLE(CollaborationStyle.class),
    WORK_STYLE(WorkStyle.class),
    PROBLEM_SOLVING_APPROACH(ProblemSolvingApproach.class),
    MBTI(Mbti.class);

    @JsonIgnore
    private final Class<? extends Enum<?>> enumClass;

    public List<Map<String, String>> getValues() {
        return switch (this) {
            case COMMUNICATION_STYLE -> Arrays.stream(CommunicationStyle.values())
                .map(style -> Map.of("name", style.name()))
                .toList();
            case COLLABORATION_STYLE -> Arrays.stream(CollaborationStyle.values())
                .map(style -> Map.of("name", style.name()))
                .toList();
            case WORK_STYLE -> Arrays.stream(WorkStyle.values())
                .map(style -> Map.of("name", style.name()))
                .toList();
            case PROBLEM_SOLVING_APPROACH -> Arrays.stream(ProblemSolvingApproach.values())
                .map(approach -> Map.of("name", approach.name()))
                .toList();
            case MBTI -> Arrays.stream(Mbti.values())
                .map(mbti -> Map.of("name", mbti.name()))
                .toList();
        };
    }
}
