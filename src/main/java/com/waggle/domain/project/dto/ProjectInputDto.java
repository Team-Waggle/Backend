package com.waggle.domain.project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.waggle.domain.reference.enums.Industry;
import com.waggle.domain.reference.enums.Skill;
import com.waggle.domain.reference.enums.WorkPeriod;
import com.waggle.domain.reference.enums.WorkWay;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import org.hibernate.validator.constraints.URL;

@Schema(description = "프로젝트 생성 dto")
public record ProjectInputDto(
    @NotBlank(message = "제목은 필수 입력 항목입니다")
    @Size(max = 50, message = "제목은 최대 50자까지 입력 가능합니다")
    @Schema(description = "제목", example = "Waggle 백엔드 모집합니다.")
    @JsonProperty("title")
    String title,

    @NotNull(message = "산업 분야는 필수 입력 항목입니다")
    @Schema(description = "관심 산업 분야", example = "FINANCE")
    @JsonProperty("industry")
    Industry industry,

    @NotNull(message = "진행 방식은 필수 입력 항목입니다")
    @Schema(description = "진행 방식", example = "ONLINE")
    @JsonProperty("work_way")
    WorkWay workWay,

    @NotNull(message = "마감 일자는 필수 입력 항목입니다")
    @FutureOrPresent(message = "마감 일자는 현재 이상의 날짜여야 합니다")
    @Schema(description = "마감 일자", example = "2021-07-01")
    @JsonProperty("recruitment_end_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate recruitmentEndDate,

    @NotNull(message = "진행 기간은 필수 입력 항목입니다")
    @Schema(description = "진행 기간", example = "ONE_MONTH")
    @JsonProperty("work_period")
    WorkPeriod workPeriod,

    @Valid
    @NotNull(message = "직무 및 인원 목록은 필수 입력 항목입니다")
    @NotEmpty(message = "최소 하나 이상의 직무 및 인원이 필요합니다")
    @Schema(description = "직무 및 인원 목록")
    @JsonProperty("recruitments")
    List<ProjectRecruitmentDto> projectRecruitmentDtos,

    @Schema(description = "사용 스킬 목록", example = "[\"JAVA\", \"AWS\"]")
    @JsonProperty("skills")
    List<Skill> skills,

    @NotBlank(message = "프로젝트 소개는 필수 입력 항목입니다")
    @Schema(description = "소개", example = "기본적으로 Spring을 쓰실 줄 알며, RestAPI를 잘 쓰시는 분을 모집합니다.")
    @JsonProperty("detail")
    String detail,

    @URL(message = "유효한 URL 형식이 아닙니다")
    @Schema(description = "연락 링크", example = "https://open.kakao.com/o/si3gRPMa")
    @JsonProperty("contact_url")
    String contactUrl,

    @URL(message = "유효한 URL 형식이 아닙니다")
    @Schema(description = "참조 링크", example = "www.naver.com")
    @JsonProperty("reference_url")
    String referenceUrl
) {

}
