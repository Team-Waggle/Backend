package com.waggle.domain.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.waggle.domain.project.entity.*;
import com.waggle.domain.reference.entity.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Builder
@Schema(description = "프로젝트 응답 dto")
public class ProjectResponseDto {

    @Schema(description = "고유값", example = "550e8400-e29b-41d4-a716-446655440000")
    @JsonProperty("id")
    private UUID id;

    @Schema(description = "제목", example = "Waggle 백엔드 모집합니다.")
    @JsonProperty("title")
    private String title;

    @Schema(description = "산업 분야")
    @JsonProperty("industry")
    private Industry industry;

    @Schema(description = "진행 방식")
    @JsonProperty("ways_of_working")
    private WaysOfWorking waysOfWorking;

    @Schema(description = "마감 일자", example = "2021-07-01T00:00:00")
    @JsonProperty("recruitment_date")
    private LocalDateTime recruitmentDate;

    @Schema(description = "진행 기간")
    @JsonProperty("duration_of_working")
    private DurationOfWorking durationOfWorking;

    @Schema(description = "모집 직무 및 인원")
    @JsonProperty("recruitment_jobs")
    private Set<ProjectRecruitmentJob> recruitmentJobs;

    @Schema(description = "참여한 멤버 직무 및 인원")
    @JsonProperty("member_jobs")
    private Set<ProjectMemberJob> memberJobs;

    @Schema(description = "사용 스킬 목록")
    @JsonProperty("skills")
    private Set<ProjectSkill> projectSkills;

    @Schema(description = "소개")
    @JsonProperty("detail")
    private String detail;

    @Schema(description = "연락 링크", example = "https://open.kakao.com/o/si3gRPMa")
    @JsonProperty("connect_url")
    private String connectUrl;

    @Schema(description = "참조 링크", example = "www.naver.com")
    @JsonProperty("reference_url")
    private String referenceUrl;

    @Schema(description = "북마크 수", example = "0")
    @JsonProperty("bookmark_cnt")
    private int bookmarkCnt;

    @Schema(description = "생성 일자", example = "2001-05-21T00:00:00")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @Schema(description = "수정 일자", example = "2025-01-19T00:00:00")
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}
