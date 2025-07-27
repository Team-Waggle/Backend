package com.waggle.domain.member.dto;

import com.waggle.domain.member.Member;
import com.waggle.domain.project.dto.ProjectResponseDto;
import com.waggle.domain.reference.enums.Position;
import com.waggle.domain.user.dto.UserResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

@Schema(description = "멤버 응답 DTO")
public record MemberResponse(
    @Schema(description = "멤버 ID", example = "42")
    Long id,

    @Schema(description = "직무", example = "{\"display_name\": \"백엔드\"}")
    Position position,

    @Schema(description = "사용자")
    UserResponseDto user,

    @Schema(description = "프로젝트")
    ProjectResponseDto project,

    @Schema(description = "멤버 합류 시간", example = "2025-06-08T10:30:00Z")
    Instant createdAt,

    @Schema(description = "멤버 직무 변경 시간", example = "2025-06-08T10:35:00Z")
    Instant updatedAt
) {

    public static MemberResponse from(Member member) {
        return new MemberResponse(
            member.getId(),
            member.getPosition(),
            null,
            null,
            member.getCreatedAt(),
            member.getUpdatedAt()
        );
    }
}
