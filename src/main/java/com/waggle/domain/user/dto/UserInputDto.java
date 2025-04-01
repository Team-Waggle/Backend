package com.waggle.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.waggle.domain.reference.enums.CollaborationStyle;
import com.waggle.domain.reference.enums.CommunicationStyle;
import com.waggle.domain.reference.enums.Industry;
import com.waggle.domain.reference.enums.Mbti;
import com.waggle.domain.reference.enums.ProblemSolvingApproach;
import com.waggle.domain.reference.enums.Sido;
import com.waggle.domain.reference.enums.Skill;
import com.waggle.domain.reference.enums.WorkStyle;
import com.waggle.domain.reference.enums.WorkTime;
import com.waggle.domain.reference.enums.WorkWay;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.DayOfWeek;
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
    Set<UserJobRoleDto> jobs,

    @JsonProperty("industries")
    @Schema(description = "관심 산업 분야 목록", example = "[\"FINANCE\", \"MEDICAL_HEALTHCARE\", \"ECOMMERCE\"]")
    Set<Industry> industries,

    @JsonProperty("skills")
    @Schema(description = "보유 기술 목록", example = "[\"JAVA\", \"TYPESCRIPT\", \"NEXT_JS\"]")
    Set<Skill> skills,

    @JsonProperty("preferred_days_of_week")
    @Schema(description = "선호 요일 목록", example = "[\"MONDAY\", \"TUESDAY\", \"WEDNESDAY\"]")
    Set<DayOfWeek> preferredDaysOfWeek,

    @JsonProperty("preferred_work_time")
    @Schema(description = "선호 시간대", example = "EVENING")
    WorkTime preferredWorkTime,

    @JsonProperty("preferred_work_way")
    @Schema(description = "선호 진행 방식", example = "OFFLINE")
    WorkWay preferredWorkWay,

    @JsonProperty("preferred_sido")
    @Schema(description = "선호 지역", example = "SEOUL")
    Sido preferredSido,

    @JsonProperty("communication_styles")
    @Schema(description = "소통 스타일", example = "[\"DYNAMIC\", \"LOGICAL_PERSUASIVE\"]")
    Set<CommunicationStyle> communicationStyles,

    @JsonProperty("collaboration_styles")
    @Schema(description = "협업 스타일", example = "[\"LEADERSHIP\", \"TEAM_FOCUSED\"]")
    Set<CollaborationStyle> collaborationStyles,

    @JsonProperty("work_styles")
    @Schema(description = "일하는 방식", example = "[\"GOAL_ORIENTED\", \"DETAIL_ORIENTED\"]")
    Set<WorkStyle> workStyles,

    @JsonProperty("problem_solving_approaches")
    @Schema(description = "문제 해결 방식", example = "[\"DATA_DRIVEN\", \"CREATIVE\"]")
    Set<ProblemSolvingApproach> problemSolvingApproaches,

    @JsonProperty("mbti")
    @Schema(description = "MBTI 유형", example = "ENFP")
    Mbti mbti,

    @JsonProperty("detail")
    @Schema(description = "자기소개 텍스트", example = "안녕하세요.")
    String detail,

    @JsonProperty("portfolio_urls")
    @Schema(description = "포트폴리오 목록")
    Set<UserPortfolioDto> portfolios
) {

}
