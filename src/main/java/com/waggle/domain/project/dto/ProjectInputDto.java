package com.waggle.domain.project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.waggle.domain.reference.enums.Industry;
import com.waggle.domain.reference.enums.Skill;
import com.waggle.domain.reference.enums.WorkPeriod;
import com.waggle.domain.reference.enums.WorkWay;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.Set;

@Schema(description = "프로젝트 생성 dto")
public record ProjectInputDto(

    @Schema(description = "제목", example = "Waggle 백엔드 모집합니다.")
    @JsonProperty("title")
    String title,

    @Schema(description = "관심 산업 분야", example = "FINANCE")
    @JsonProperty("industry")
    Industry industry,

    @Schema(description = "진행 방식", example = "ONLINE")
    @JsonProperty("work_way")
    WorkWay workWay,

    @Schema(description = "마감 일자", example = "2021-07-01")
    @JsonProperty("recruitment_end_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate recruitmentEndDate,

    @Schema(description = "진행 기간", example = "ONE_MONTH")
    @JsonProperty("work_period")
    WorkPeriod workPeriod,

    @Schema(description = "모집 직무 및 인원")
    @JsonProperty("recruitment_jobs")
    Set<ProjectJobInputDto> recruitmentJobs,

    @Schema(description = "참여한 멤버 직무 및 인원")
    @JsonProperty("member_jobs")
    Set<ProjectJobInputDto> memberJobs,

    @Schema(description = "사용 스킬 목록", example = "[\"JAVA\", \"AWS\"]")
    @JsonProperty("skills")
    Set<Skill> skills,

    @Schema(description = "소개", example = "기본적으로 Spring을 쓰실 줄 알며, RestAPI를 잘 쓰시는 분을 모집합니다.")
    @JsonProperty("detail")
    String detail,

    @Schema(description = "연락 링크", example = "https://open.kakao.com/o/si3gRPMa")
    @JsonProperty("contact_url")
    String contactUrl,

    @Schema(description = "참조 링크", example = "www.naver.com")
    @JsonProperty("reference_url")
    String referenceUrl
) {

}
