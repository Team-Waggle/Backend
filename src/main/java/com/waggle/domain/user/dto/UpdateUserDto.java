package com.waggle.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Getter
@Schema(description = "사용자 정보 수정을 위한 DTO")
public class UpdateUserDto {

    @JsonProperty("profile_image")
    @Schema(description = "프로필 이미지 URL", type = "string", format = "binary")
    private MultipartFile profileImage;

    @JsonProperty("name")
    @Schema(description = "사용자 이름", example = "홍길동")
    private String name;

    @JsonProperty("jobs")
    @Schema(description = "직무 및 경력 목록")
    private Set<UserJobDto> jobs;

    @JsonProperty("industry_ids")
    @Schema(description = "관심 산업 분야 고유키 목록", example = "[1, 3, 5]")
    private Set<Long> industries;

    @JsonProperty("skill_ids")
    @Schema(description = "보유 기술 고유키 목록", example = "[2, 4, 7]")
    private Set<Long> skills;

    @JsonProperty("prefer_week_days_ids")
    @Schema(description = "선호 요일 고유키 목록", example = "[1, 2, 3]")
    private Set<Long> preferWeekDays;

    @JsonProperty("prefer_tow_id")
    @Schema(description = "선호 시간대 고유키", example = "1")
    private Long preferTowId;

    @JsonProperty("prefer_wow_id")
    @Schema(description = "선호 진행 방식 고유키", example = "2")
    private Long preferWowId;

    @JsonProperty("prefer_sido_code")
    @Schema(description = "선호 지역 고유키", example = "11")
    private String preferSidoId;

    @JsonProperty("introduce_ids")
    @Schema(description = "자기소개 키워드", example = "[3,13,26,31,45]")
    private Set<Long> introduces;

    @JsonProperty("detail")
    @Schema(description = "자기소개 텍스트", example = "안녕하세요.")
    private String detail;

    @JsonProperty("portfolio_urls")
    @Schema(description = "포트폴리오 링크")
    private Set<UserPortfolioUrlDto> portfolioUrls;
}
