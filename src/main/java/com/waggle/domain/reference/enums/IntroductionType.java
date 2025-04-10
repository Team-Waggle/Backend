package com.waggle.domain.reference.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum IntroductionType {
    COMMUNICATION_STYLE("소통 스타일", CommunicationStyle.class),
    COLLABORATION_STYLE("협업 성향", CollaborationStyle.class),
    WORK_STYLE("작업 방식", WorkStyle.class),
    PROBLEM_SOLVING_APPROACH("문제 해결 방식", ProblemSolvingApproach.class),
    MBTI("MBTI", Mbti.class);

    @JsonProperty("display_name")
    private final String displayName;

    @JsonIgnore
    private final Class<? extends Enum<?>> enumClass;

    public List<Map<String, String>> getValues() {
        return switch (this) {
            case COMMUNICATION_STYLE -> Arrays.stream(CommunicationStyle.values())
                .map(style -> Map.of("name", style.name(), "display_name", style.getDisplayName()))
                .toList();
            case COLLABORATION_STYLE -> Arrays.stream(CollaborationStyle.values())
                .map(style -> Map.of("name", style.name(), "display_name", style.getDisplayName()))
                .toList();
            case WORK_STYLE -> Arrays.stream(WorkStyle.values())
                .map(style -> Map.of("name", style.name(), "display_name", style.getDisplayName()))
                .toList();
            case PROBLEM_SOLVING_APPROACH -> Arrays.stream(ProblemSolvingApproach.values())
                .map(approach -> Map.of("name", approach.name(), "display_name",
                    approach.getDisplayName()))
                .toList();
            case MBTI -> Arrays.stream(Mbti.values())
                .map(mbti -> Map.of("name", mbti.name(), "display_name", mbti.getDisplayName()))
                .toList();
        };
    }
}
