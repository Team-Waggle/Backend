package com.waggle.domain.application.dto;

import com.waggle.domain.application.ApplicationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "지원 상태 변경 DTO")
public record UpdateStatusDto(
    @Schema(description = "지원 상태", example = "APPROVED")
    @NotNull(message = "status must not be null")
    ApplicationStatus status
) {

}
