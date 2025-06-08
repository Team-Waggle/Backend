package com.waggle.domain.member.dto;

import com.waggle.domain.reference.enums.Position;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "직무 변경 DTO")
public record UpdatePositionDto(
    @Schema(description = "직무", example = "FRONTEND")
    @NotNull(message = "position must not be null")
    Position position
) {

}
