package com.waggle.domain.reference.controller;

import com.waggle.domain.reference.service.ReferenceService;
import com.waggle.global.response.ApiStatus;
import com.waggle.global.response.BaseResponse;
import com.waggle.global.response.ErrorResponse;
import com.waggle.global.response.SuccessResponse;
import com.waggle.global.response.swagger.DurationOfWorkingsSuccessResponse;
import com.waggle.global.response.swagger.IndustriesSuccessResponse;
import com.waggle.global.response.swagger.JobsSuccessResponse;
import com.waggle.global.response.swagger.MainIntroducesSuccessResponse;
import com.waggle.global.response.swagger.PortfolioUrlsSuccessResponse;
import com.waggle.global.response.swagger.SidosSuccessResponse;
import com.waggle.global.response.swagger.SkillsSuccessResponse;
import com.waggle.global.response.swagger.SubIntroducesSuccessResponse;
import com.waggle.global.response.swagger.TimeOfWorkingsSuccessResponse;
import com.waggle.global.response.swagger.WaysOfWorkingsSuccessResponse;
import com.waggle.global.response.swagger.WeekDaysSuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "참조 데이터", description = "참조 데이터 관련 API")
@RestController
@RequestMapping("api/v1/reference")
@RequiredArgsConstructor
public class ReferenceController {

    private final ReferenceService referenceService;

    @GetMapping("/industrial")
    @Operation(summary = "산업 분야 조회", description = "산업 분야를 전부 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "산업 분야 조회 성공", content = @Content(schema = @Schema(implementation = IndustriesSuccessResponse.class))),
        @ApiResponse(responseCode = "404", description = "산업 분야가 존재하지 않음", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<BaseResponse<Object>> fetchIndustrial() {
        return SuccessResponse.of(ApiStatus._OK, referenceService.getIndustrials());
    }

    @GetMapping("/skill")
    @Operation(summary = "사용 기술 조회", description = "사용 기술을 전부 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "기술 조회 성공", content = @Content(schema = @Schema(implementation = SkillsSuccessResponse.class))),
        @ApiResponse(responseCode = "404", description = "기술이 존재하지 않음", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<BaseResponse<Object>> fetchSkill() {
        return SuccessResponse.of(ApiStatus._OK, referenceService.getSkills());
    }

    @GetMapping("/job")
    @Operation(summary = "직무 조회", description = "직무를 전부 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "직무 조회 성공", content = @Content(schema = @Schema(implementation = JobsSuccessResponse.class))),
        @ApiResponse(responseCode = "404", description = "직무가 존재하지 않음", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<BaseResponse<Object>> fetchJob() {
        return SuccessResponse.of(ApiStatus._OK, referenceService.getJobs());
    }

    @GetMapping("/week-days")
    @Operation(summary = "요일 조회", description = "요일 목록을 전부 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "요일 조회 성공", content = @Content(schema = @Schema(implementation = WeekDaysSuccessResponse.class))),
        @ApiResponse(responseCode = "404", description = "요일이 존재하지 않음", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<BaseResponse<Object>> fetchWeekDays() {
        return SuccessResponse.of(ApiStatus._OK, referenceService.getWeekDays());
    }

    @GetMapping("/tow")
    @Operation(summary = "진행 시간대 조회", description = "진행 시간대 목록을 전부 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "진행 시간대 조회 성공", content = @Content(schema = @Schema(implementation = TimeOfWorkingsSuccessResponse.class))),
        @ApiResponse(responseCode = "404", description = "진행 시간대가 존재하지 않음", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<BaseResponse<Object>> fetchTimezone() {
        return SuccessResponse.of(ApiStatus._OK, referenceService.getTimeOfWorkings());
    }

    @GetMapping("/dow")
    @Operation(summary = "진행 기간 조회", description = "진행 기간 목록을 전부 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "작업 기간 조회 성공", content = @Content(schema = @Schema(implementation = DurationOfWorkingsSuccessResponse.class))),
        @ApiResponse(responseCode = "404", description = "작업 기간이 존재하지 않음", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<BaseResponse<Object>> fetchDurationOfWorking() {
        return SuccessResponse.of(ApiStatus._OK, referenceService.getDurationOfWorkings());
    }

    @GetMapping("/wow")
    @Operation(summary = "진행 방식 조회", description = "진행 방식 목록을 전부 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "근무 방식 조회 성공", content = @Content(schema = @Schema(implementation = WaysOfWorkingsSuccessResponse.class))),
        @ApiResponse(responseCode = "404", description = "근무 방식이 존재하지 않음", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<BaseResponse<Object>> fetchWaysOfWorking() {
        return SuccessResponse.of(ApiStatus._OK, referenceService.getWaysOfWorkings());
    }

    @GetMapping("/area/sido")
    @Operation(summary = "시/도 조회", description = "시/도 목록을 전부 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "시/도 조회 성공", content = @Content(schema = @Schema(implementation = SidosSuccessResponse.class))),
        @ApiResponse(responseCode = "404", description = "시/도가 존재하지 않음", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<BaseResponse<Object>> fetchSido() {
        return SuccessResponse.of(ApiStatus._OK, referenceService.getSidoes());
    }

    @GetMapping("/portfolio-url")
    @Operation(summary = "포트폴리오 링크 종류 조회", description = "포트폴리오 링크 종류를 전부 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "포트폴리오 링크 종류 조회 성공", content = @Content(schema = @Schema(implementation = PortfolioUrlsSuccessResponse.class))),
        @ApiResponse(responseCode = "404", description = "포트폴리오 링크 종류가 존재하지 않음", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<BaseResponse<Object>> fetchPortfolioUrl() {
        return SuccessResponse.of(ApiStatus._OK, referenceService.getPortfolioUrls());
    }

    @GetMapping("/main-introduce")
    @Operation(summary = "자기소개 키워드 대분류 조회", description = "자기소개 키워드의 대분류 목록을 전부 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "대분류 키워드 조회 성공", content = @Content(schema = @Schema(implementation = MainIntroducesSuccessResponse.class))),
        @ApiResponse(responseCode = "404", description = "자기소개 대분류 키워드가 존재하지 않음", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<BaseResponse<Object>> fetchMainIntroduce() {
        return SuccessResponse.of(ApiStatus._OK, referenceService.getMainIntroduces());
    }

    @GetMapping("/sub-introduce")
    @Operation(summary = "자기소개 키워드 소분류 조회", description = "자기소개 키워드의 소분류 목록을 전부 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "자기소개 소분류 키워드 조회 성공", content = @Content(schema = @Schema(implementation = SubIntroducesSuccessResponse.class))),
        @ApiResponse(responseCode = "404", description = "자기소개 소분류 키워드가 존재하지 않음", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<BaseResponse<Object>> fetchSubIntroduce() {
        return SuccessResponse.of(ApiStatus._OK, referenceService.getSubIntroduces());
    }
}
