package com.waggle.domain.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UpsertPostDto(
    @Schema(description = "제목", example = "팀 Waggle에서 개발자를 모집합니다.")
    @NotBlank(message = "title must be not blank")
    String title,

    @Schema(description = "내용", example = """
        팀 Waggle에서
        프론트엔드 개발자 2명
        백엔드 개발자 1명을 모집합니다.
        """)
    @NotBlank(message = "content must be not blank")
    String content,

    @Schema(description = "프로젝트 ID (선택사항)", example = "42")
    Long projectId
) {

}
