package com.waggle.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;
import lombok.Builder;

@Builder
@Schema(description = "사용자 정보 수정 DTO")
public record UserInputDto(

    @JsonProperty("name")
    @Schema(description = "사용자 이름", example = "홍길동")
    String name,

    @JsonProperty("jobs")
    @Schema(description = "직무 및 경력 목록")
    Set<UserJobDto> jobs,

    @JsonProperty("industry_ids")
    @Schema(description = "관심 산업 분야 고유키 목록", example = "[1, 3, 5]")
    Set<Long> industries,

    @JsonProperty("skill_ids")
    @Schema(description = "보유 기술 고유키 목록", example = "[2, 4, 7]")
    Set<Long> skills,

    @JsonProperty("prefer_week_days_ids")
    @Schema(description = "선호 요일 고유키 목록", example = "[1, 2, 3]")
    Set<Long> preferWeekDays,

    @JsonProperty("prefer_tow_id")
    @Schema(description = "선호 시간대 고유키", example = "1")
    Long preferTowId,

    @JsonProperty("prefer_wow_id")
    @Schema(description = "선호 진행 방식 고유키", example = "2")
    Long preferWowId,

    @JsonProperty("prefer_sido_code")
    @Schema(description = "선호 지역 고유키", example = "11")
    String preferSidoId,

    @JsonProperty("introduce_ids")
    @Schema(description = "자기소개 키워드", example = "[3,13,26,31,45]")
    Set<Long> introduces,

    @JsonProperty("detail")
    @Schema(description = "자기소개 텍스트", example = "안녕하세요.")
    String detail,

    @JsonProperty("portfolio_urls")
    @Schema(description = "포트폴리오 링크")
    Set<UserPortfolioUrlDto> portfolioUrls
) {

}
