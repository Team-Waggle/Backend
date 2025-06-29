package com.waggle.domain.projectV2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "프로젝트 생성 및 수정 요청 DTO")
public record UpsertProjectRequest(
    @Schema(description = "프로젝트 이름", example = "waggle")
    @NotBlank(message = "name must not be blank")
    @Size(max = 30, message = "name must not exceed 30 characters")
    String name,

    @Schema(description = "프로젝트 태그라인", example = "프로젝트 팀원 모집 웹 애플리케이션")
    @NotBlank(message = "tagline must not be blank")
    @Size(max = 100, message = "tagline must not exceed 100 characters")
    String tagline,

    @Schema(description = "프로젝트 설명")
    @NotBlank(message = "description must not be blank")
    String description
) {

}
