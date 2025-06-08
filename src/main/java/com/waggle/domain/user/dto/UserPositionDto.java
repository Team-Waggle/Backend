package com.waggle.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.waggle.domain.reference.enums.Position;
import com.waggle.domain.user.entity.UserPosition;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UserPositionDto(
    @NotNull(message = "직무는 필수 항목입니다")
    @JsonProperty("job_role")
    @Schema(description = "직무", example = "BACKEND")
    Position position,

    @Min(value = 0, message = "경력은 0년 이상이어야 합니다")
    @JsonProperty("year_count")
    @Schema(description = "경력", example = "3")
    int yearCount
) {

    public static UserPositionDto from(UserPosition userPosition) {
        return new UserPositionDto(userPosition.getPosition(), userPosition.getYearCount());
    }
}
