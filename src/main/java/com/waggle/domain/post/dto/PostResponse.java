package com.waggle.domain.post.dto;

import com.waggle.domain.post.Post;
import com.waggle.domain.projectV2.dto.ProjectResponse;
import com.waggle.domain.user.dto.UserResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "게시글 응답 DTO")
public record PostResponse(
    @Schema(description = "게시글 ID", example = "42")
    Long id,

    @Schema(description = "게시글 제목", example = "팀 Waggle에서 개발자를 모집합니다.")
    String title,

    @Schema(description = "게시글 내용", example = "프론트엔드 개발자 2명, 백엔드 개발자 1명을 모집합니다.")
    String content,

    @Schema(description = "작성자")
    UserResponseDto user,

    @Schema(description = "연결된 프로젝트 정보 (선택사항)", nullable = true)
    ProjectResponse project,

    @Schema(description = "게시글 생성 시간", example = "2025-06-08T10:30:00Z")
    LocalDateTime createdAt,

    @Schema(description = "게시글 수정 시간", example = "2025-06-08T10:35:00Z")
    LocalDateTime updatedAt
) {

    public static PostResponse from(Post post) {
        return new PostResponse(
            post.getId(),
            post.getTitle(),
            post.getContent(),
            null,
            post.getProject() != null ? ProjectResponse.from(post.getProject()) : null,
            post.getCreatedAt(),
            post.getUpdatedAt()
        );
    }
}
