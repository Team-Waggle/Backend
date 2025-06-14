package com.waggle.domain.recruitment.dto;

import com.waggle.domain.recruitment.Recruitment;
import com.waggle.domain.reference.enums.Position;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "모집 응답 DTO")
public record RecruitmentResponse(
    Long id,
    Position position,
    int remainingCount,
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
