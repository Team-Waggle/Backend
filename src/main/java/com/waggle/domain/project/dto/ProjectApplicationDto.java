package com.waggle.domain.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.waggle.domain.reference.enums.Position;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record ProjectApplicationDto(
    @NotNull(message = "지원 직무는 필수 입력 항목입니다")
    @Schema(description = "지원 직무", example = "BACKEND")
    @JsonProperty("job_role")
    Position position
) {

}
