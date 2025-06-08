package com.waggle.domain.application.controller;

import com.waggle.domain.application.Application;
import com.waggle.domain.application.ApplicationStatus;
import com.waggle.domain.application.dto.ApplicationResponse;
import com.waggle.domain.application.dto.UpdateStatusDto;
import com.waggle.domain.application.service.ApplicationService;
import com.waggle.global.response.ApiStatus;
import com.waggle.global.response.BaseResponse;
import com.waggle.global.response.SuccessResponse;
import com.waggle.global.secure.oauth2.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
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

@RequiredArgsConstructor
@RequestMapping("/api/v2/applications")
@RestController
public class ApplicationController {

    private final ApplicationService applicationService;

    @Operation(
        summary = "내 지원 목록 조회",
        description = "인증된 사용자의 지원 목록을 조회합니다."
    )
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
        description = "지원 상태를 변경합니다."
    )
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
