package com.waggle.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.waggle.domain.reference.enums.Sido;
import com.waggle.domain.reference.enums.WorkTime;
import com.waggle.domain.reference.enums.WorkWay;
import com.waggle.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(
    @Schema(description = "사용자 ID", example = "550e8400-e29b-41d4-a716-446655440000")
    @JsonProperty("id")
    UUID id,

    @Schema(description = "사용자 이름", example = "홍길동")
    @JsonProperty("name")
    String name,

    @Schema(description = "사용자 이메일", example = "hong@gmail.com")
    @JsonProperty("email")
    String email,

    @Schema(description = "프로필 이미지 URL", example = "https://example.com/profile.jpg")
    @JsonProperty("profile_image_url")
    String profileImageUrl,

    @Schema(description = "선호 작업 시간", example = "{\"display_name\": \"오전\"}")
    @JsonProperty("work_time")
    WorkTime workTime,

    @Schema(description = "선호 작업 방식", example = "{\"display_name\": \"온라인\"}")
    @JsonProperty("work_way")
    WorkWay workWay,

    @Schema(description = "선호 지역", example = "{\"display_name\": \"서울특별시\"}")
    @JsonProperty("sido")
    Sido sido,

    @Schema(description = "자기소개", example = "안녕하세요. 백엔드 개발자입니다.")
    @JsonProperty("detail")
    String detail,

    @Schema(description = "생성일자", example = "2025-07-27T10:30:00Z")
    @JsonProperty("created_at")
    LocalDateTime createdAt,

    @Schema(description = "수정일자", example = "2025-07-27T10:30:00Z")
    @JsonProperty("updated_at")
    LocalDateTime updatedAt
) {

    public static UserResponse from(User user) {
        return new UserResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getProfileImageUrl(),
            user.getWorkTime(),
            user.getWorkWay(),
            user.getSido(),
            user.getDetail(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
}
