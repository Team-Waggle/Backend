package com.waggle.domain.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.waggle.domain.project.ProjectInfo;
import com.waggle.domain.project.entity.ProjectSkill;
import com.waggle.domain.reference.enums.Industry;
import com.waggle.domain.reference.enums.WorkPeriod;
import com.waggle.domain.reference.enums.WorkWay;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Schema(description = "프로젝트 응답 dto")
public record ProjectResponseDto(
    @Schema(description = "고유값", example = "1")
    @JsonProperty("id")
    Long id,

    @Schema(description = "제목", example = "Waggle 백엔드 모집합니다.")
    @JsonProperty("title")
    String title,

    @Schema(description = "산업 분야")
    @JsonProperty("industry")
    Industry industry,

    @Schema(description = "진행 방식")
    @JsonProperty("ways_of_working")
    WorkWay workWay,

    @Schema(description = "마감 일자", example = "2021-07-01")
    @JsonProperty("recruitment_end_date")
    LocalDate recruitmentEndDate,

    @Schema(description = "진행 기간")
    @JsonProperty("work_period")
    WorkPeriod workPeriod,

    @Schema(description = "직무 및 인원")
    @JsonProperty("recruitments")
    List<ProjectRecruitmentDto> projectRecruitmentDtos,

    @Schema(description = "사용 스킬 목록")
    @JsonProperty("skills")
    List<ProjectSkill> projectSkills,

    @Schema(description = "소개")
    @JsonProperty("detail")
    String detail,

    @Schema(description = "연락 링크", example = "https://open.kakao.com/o/si3gRPMa")
    @JsonProperty("connect_url")
    String connectUrl,

    @Schema(description = "참조 링크", example = "www.naver.com")
    @JsonProperty("reference_url")
    String referenceUrl,

    @Schema(description = "북마크 수", example = "0")
    @JsonProperty("bookmark_cnt")
    int bookmarkCnt,

    @Schema(description = "생성 일자", example = "2001-05-21T00:00:00")
    @JsonProperty("created_at")
    LocalDateTime createdAt,

    @Schema(description = "수정 일자", example = "2025-01-19T00:00:00")
    @JsonProperty("updated_at")
    LocalDateTime updatedAt
) {

    public static ProjectResponseDto from(ProjectInfo projectInfo) {
        return new ProjectResponseDto(
            projectInfo.project().getId(),
            projectInfo.project().getTitle(),
            projectInfo.project().getIndustry(),
            projectInfo.project().getWorkWay(),
            projectInfo.project().getRecruitmentEndDate(),
            projectInfo.project().getWorkPeriod(),
            projectInfo.projectRecruitments().stream()
                .map(ProjectRecruitmentDto::from)
                .sorted(Comparator.comparing(project -> project.jobRole().name()))
                .toList(),
            projectInfo.projectSkills().stream()
                .sorted(Comparator.comparing(project -> project.getSkill().name()))
                .toList(),
            projectInfo.project().getDetail(),
            projectInfo.project().getContactUrl(),
            projectInfo.project().getReferenceUrl(),
            projectInfo.project().getBookmarkCount(),
            projectInfo.project().getCreatedAt(),
            projectInfo.project().getUpdatedAt()
        );
    }
}
