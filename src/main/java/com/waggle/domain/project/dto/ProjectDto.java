package com.waggle.domain.project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Schema(description = "프로젝트 생성 dto")
public class ProjectDto {

    @Schema(description = "제목", example = "Waggle 백엔드 모집합니다.")
    @JsonProperty("title")
    private String title;

    @Schema(description = "관심 산업 분야 고유키", example = "2")
    @JsonProperty("industry_id")
    private Long industryId;

    @Schema(description = "진행 방식 고유키", example = "1")
    @JsonProperty("way_of_working_id")
    private Long wayOfWorkingId;

    @Schema(description = "마감 일자", example = "2021-07-01T00:00:00")
    @JsonProperty("recruitment_date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime recruitmentDate;

    @Schema(description = "진행 기간 고유키", example = "1")
    @JsonProperty("duration_of_working_id")
    private Long durationOfWorkingId;

    @Schema(description = "모집 직무 및 인원")
    @JsonProperty("jobs")
    private Set<ProjectJobDto> jobs;

    @Schema(description = "사용 스킬 고유키 목록", example = "[2, 4, 7]")
    @JsonProperty("skill_ids")
    private Set<Long> skillIds;

    @Schema(description = "소개", example = "기본적으로 Spring을 쓰실 줄 알며, RestAPI를 잘 쓰시는 분을 모집합니다.")
    @JsonProperty("detail")
    private String detail;

    @Schema(description = "연락 링크", example = "https://open.kakao.com/o/si3gRPMa")
    @JsonProperty("connect_url")
    private String connectUrl;

    @Schema(description = "참조 링크", example = "www.naver.com")
    @JsonProperty("reference_url")
    private String referenceUrl;
}
