package com.waggle.domain.application.dto;

import com.waggle.domain.reference.enums.Position;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "프로젝트 지원 요청 DTO")
public record CreateApplicationDto(
    @Schema(description = "지원 포지션", example = "BACKEND")
    @NotNull(message = "position must not be null")
    Position position
) {

}
