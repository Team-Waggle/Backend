package com.waggle.domain.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.waggle.domain.reference.enums.Position;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "포지션별 지원자 수")
public record PositionApplicantCountDto(
    @Schema(description = "포지션", example = "FRONTEND")
    @JsonProperty("position")
    Position position,

    @Schema(description = "지원자 수", example = "3")
    @JsonProperty("count")
    int count
) {

    public static PositionApplicantCountDto of(Position position, int count) {
        return new PositionApplicantCountDto(position, count);
    }
}
