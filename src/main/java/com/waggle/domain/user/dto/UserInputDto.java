package com.waggle.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.waggle.domain.reference.enums.Industry;
import com.waggle.domain.reference.enums.Sido;
import com.waggle.domain.reference.enums.Skill;
import com.waggle.domain.reference.enums.WorkTime;
import com.waggle.domain.reference.enums.WorkWay;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.DayOfWeek;
import java.util.List;
import lombok.Builder;

@Builder
@Schema(description = "사용자 정보 수정 DTO")
public record UserInputDto(
    @NotBlank(message = "이름은 필수 항목입니다")
    @Size(min = 2, max = 50, message = "이름은 2자 이상 50자 이하여야 합니다")
    @JsonProperty("name")
    @Schema(description = "사용자 이름", example = "홍길동")
    String name,

    @Valid
    @JsonProperty("positions")
    @Schema(description = "직무 및 경력 목록")
    List<UserPositionDto> positions,

    @Size(max = 5, message = "관심 산업 분야는 최대 5개까지 선택 가능합니다")
    @JsonProperty("industries")
    @Schema(description = "관심 산업 분야 목록", example = "[\"FINANCE\", \"MEDICAL_HEALTHCARE\", \"ECOMMERCE\"]")
    List<Industry> industries,

    @JsonProperty("skills")
    @Schema(description = "보유 기술 목록", example = "[\"JAVA\", \"TYPESCRIPT\", \"NEXT_JS\"]")
    List<Skill> skills,

    @JsonProperty("preferred_days_of_week")
    @Schema(description = "선호 요일 목록", example = "[\"MONDAY\", \"TUESDAY\", \"WEDNESDAY\"]")
    List<DayOfWeek> daysOfWeek,

    @JsonProperty("preferred_work_time")
    @Schema(description = "선호 시간대", example = "EVENING")
    WorkTime workTime,

    @JsonProperty("preferred_work_way")
    @Schema(description = "선호 진행 방식", example = "OFFLINE")
    WorkWay workWay,

    @JsonProperty("preferred_sido")
    @Schema(description = "선호 지역", example = "SEOUL")
    Sido sido,

    @Valid
    @JsonProperty("introduction")
    @Schema(description = "사용자 소개")
    UserIntroductionDto introduction,

    @Size(max = 1000, message = "자기소개는 1000자 이하로 작성해주세요")
    @JsonProperty("detail")
    @Schema(description = "자기소개 텍스트", example = "안녕하세요.")
    String detail,

    @Valid
    @JsonProperty("portfolios")
    @Schema(description = "포트폴리오 목록")
    List<UserPortfolioDto> portfolios
) {

}
