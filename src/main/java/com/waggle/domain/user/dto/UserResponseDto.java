package com.waggle.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.waggle.domain.reference.enums.Sido;
import com.waggle.domain.reference.enums.WorkTime;
import com.waggle.domain.reference.enums.WorkWay;
import com.waggle.domain.user.entity.User;
import com.waggle.domain.user.entity.UserDayOfWeek;
import com.waggle.domain.user.entity.UserIndustry;
import com.waggle.domain.user.entity.UserIntroduction;
import com.waggle.domain.user.entity.UserJobRole;
import com.waggle.domain.user.entity.UserPortfolio;
import com.waggle.domain.user.entity.UserSkill;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Builder;

@Builder
@Schema(description = "사용자 정보 응답 DTO")
public record UserResponseDto(

    @Schema(description = "사용자 ID", example = "550e8400-e29b-41d4-a716-446655440000")
    @JsonProperty("id")
    UUID id,

    @Schema(description = "OAuth 제공자", example = "google")
    @JsonProperty("provider")
    String provider,

    @Schema(description = "OAuth ID", example = "1234567890")
    @JsonProperty("provider_id")
    String providerId,

    @Schema(description = "프로필 이미지 URL")
    @JsonProperty("profile_img_url")
    String profileImageUrl,

    @Schema(description = "사용자 이름", example = "홍길동")
    @JsonProperty("name")
    String name,

    @Schema(description = "사용자 이메일", example = "hong@gmail.com")
    @JsonProperty("email")
    String email,

    @Schema(description = "사용자 직무 정보")
    @JsonProperty("jobs")
    Set<UserJobRole> userJobRoles,

    @Schema(description = "사용자 관심 산업 정보")
    @JsonProperty("industries")
    Set<UserIndustry> userIndustries,

    @Schema(description = "사용자 보유 기술 정보")
    @JsonProperty("skills")
    Set<UserSkill> userSkills,

    @Schema(description = "사용자 선호 요일 정보")
    @JsonProperty("days_of_week")
    Set<UserDayOfWeek> userDaysOfWeek,

    @Schema(description = "사용자 선호 작업 시간 정보")
    @JsonProperty("preferred_work_time")
    WorkTime preferredWorkTime,

    @Schema(description = "사용자 선호 작업 방식 정보")
    @JsonProperty("preferred_work_way")
    WorkWay preferredWorkWay,

    @Schema(description = "사용자 지역 정보")
    @JsonProperty("preferred_sido")
    Sido preferredSido,

    @Schema(description = "사용자 소개 키워드 정보")
    @JsonProperty("introduces")
    Set<UserIntroduction> userIntroductions,

    @Schema(description = "사용자 자기소개", example = "안녕하세요.")
    @JsonProperty("detail")
    String detail,

    @Schema(description = "사용자 포트폴리오 링크 정보")
    @JsonProperty("portfolio_urls")
    Set<UserPortfolio> userPortfolios,

    @Schema(description = "생성일자", example = "2021-07-01T00:00:00")
    @JsonProperty("created_at")
    LocalDateTime createdAt,

    @Schema(description = "수정일자", example = "2021-07-01T00:00:00")
    @JsonProperty("updated_at")
    LocalDateTime updatedAt
) {

    public static UserResponseDto from(User user) {
        return UserResponseDto.builder()
            .id(user.getId())
            .provider(user.getProvider())
            .providerId(user.getProviderId())
            .profileImageUrl(user.getProfileImageUrl())
            .name(user.getName())
            .email(user.getEmail())
            .userJobRoles(user.getUserJobRoles().stream()
                .sorted(Comparator.comparing(uj -> uj.getJobRole().name()))
                .collect(Collectors.toCollection(LinkedHashSet::new)))
            .userIndustries(user.getUserIndustries().stream()
                .sorted(Comparator.comparing(ui -> ui.getIndustry().name()))
                .collect(Collectors.toCollection(LinkedHashSet::new)))
            .userSkills(user.getUserSkills().stream()
                .sorted(Comparator.comparing(us -> us.getSkill().name()))
                .collect(Collectors.toCollection(LinkedHashSet::new)))
            .userDaysOfWeek(user.getUserDayOfWeeks().stream()
                .sorted(Comparator.comparing(uwd -> uwd.getDayOfWeek().name()))
                .collect(Collectors.toCollection(LinkedHashSet::new)))
            .preferredWorkTime(user.getPreferredWorkTime())
            .preferredWorkWay(user.getPreferredWorkWay())
            .preferredSido(user.getPreferredSido())
//            .userIntroduces(user.getUserIntroduces().stream()
//                .sorted(Comparator.comparing(ui -> ui.getSubIntroduce().getId()))
//                .collect(Collectors.toCollection(LinkedHashSet::new)))
            .detail(user.getDetail())
            .userPortfolios(user.getUserPortfolios().stream()
                .sorted(Comparator.comparing(upu -> upu.getPortfolioType().name()))
                .collect(Collectors.toCollection(LinkedHashSet::new)))
            .createdAt(user.getCreatedAt())
            .updatedAt(user.getUpdatedAt())
            .build();
    }
}
