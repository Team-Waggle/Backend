package com.waggle.domain.recruitment.dto;

import com.waggle.domain.recruitment.Recruitment;
import com.waggle.domain.reference.enums.Position;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "모집 응답 DTO")
public record RecruitmentResponse(
    @Schema(description = "모집 ID", example = "42")
    Long id,

    @Schema(description = "직무", example = "{\"display_name\": \"백엔드\"}")
    Position position,

    @Schema(description = "해당 직무 모집 명수", example = "1")
    int remainingCount,

    @Schema(description = "해당 직무 팀원 명수", example = "2")
    int currentCount
) {

    public static RecruitmentResponse from(Recruitment recruitment) {
        return new RecruitmentResponse(
            recruitment.getId(),
            recruitment.getPosition(),
            recruitment.getRemainingCount(),
            recruitment.getCurrentCount()
        );
    }
}
