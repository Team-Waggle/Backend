package com.waggle.domain.application.controller;

import com.waggle.domain.application.Application;
import com.waggle.domain.application.ApplicationStatus;
import com.waggle.domain.application.dto.ApplicationResponse;
import com.waggle.domain.application.dto.UpdateStatusDto;
import com.waggle.domain.application.service.ApplicationService;
import com.waggle.global.response.ApiStatus;
import com.waggle.global.response.BaseResponse;
import com.waggle.global.response.ErrorResponse;
import com.waggle.global.response.SuccessResponse;
import com.waggle.global.response.swagger.ApplicationSuccessResponse;
import com.waggle.global.response.swagger.ApplicationsSuccessResponse;
import com.waggle.global.secure.oauth2.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Application", description = "지원 관리 API")
@RequiredArgsConstructor
@RequestMapping("/api/v2/applications")
@RestController
public class ApplicationController {

    private final ApplicationService applicationService;

    @Operation(
        summary = "내 지원 목록 조회",
        description = "인증된 사용자의 지원 목록을 조회합니다."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "지원 목록 조회 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApplicationsSuccessResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    @GetMapping("/me")
    public ResponseEntity<BaseResponse<List<ApplicationResponse>>> getMyApplications(
        @RequestParam(required = false) ApplicationStatus status,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        List<Application> applications = applicationService.getMyApplications(
            status,
            userDetails.getUser()
        );

        return SuccessResponse.of(
            ApiStatus._OK,
            applications.stream().map(ApplicationResponse::from).toList()
        );
    }

    @Operation(
        summary = "지원 상태 변경",
        description = "지원 상태를 변경합니다. 지원 상태를 변경합니다. 승인/거절은 프로젝트 리더만, 취소는 지원자만 가능합니다."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "지원 상태 변경 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApplicationSuccessResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청 데이터",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "접근 권한 없음",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = {
                    @ExampleObject(
                        name = "프로젝트 접근 권한 없음",
                        description = "승인/거절 시 프로젝트 리더가 아닌 경우",
                        value = """
                            {
                                "isSuccess": false,
                                "code": 403,
                                "message": "Access denied to project with id: a1b2c3d4-e5f6-7890-abcd-ef1234567890",
                                "payload": null,
                                "timestamp": "2025-06-08T10:30:00Z"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "지원 접근 권한 없음",
                        description = "취소 시 지원자가 아닌 경우",
                        value = """
                            {
                                "isSuccess": false,
                                "code": 403,
                                "message": "Access denied to application with id: 123",
                                "payload": null,
                                "timestamp": "2025-06-08T10:30:00Z"
                            }
                            """
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "존재하지 않는 지원",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "422",
            description = "처리할 수 없는 상태 변경",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    @PatchMapping("/{applicationId}")
    public ResponseEntity<BaseResponse<ApplicationResponse>> updateApplicationStatus(
        @PathVariable Long applicationId,
        @Valid @RequestBody UpdateStatusDto updateStatusDto,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Application application = applicationService.updateApplicationStatus(
            applicationId,
            updateStatusDto,
            userDetails.getUser()
        );

        return SuccessResponse.of(
            ApiStatus._OK,
            ApplicationResponse.from(application)
        );
    }
}
