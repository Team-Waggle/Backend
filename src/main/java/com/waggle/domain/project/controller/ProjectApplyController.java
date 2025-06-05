package com.waggle.domain.project.controller;

import com.waggle.domain.project.ProjectInfo;
import com.waggle.domain.project.dto.ProjectApplicationDto;
import com.waggle.domain.project.dto.ProjectResponseDto;
import com.waggle.domain.project.entity.Project;
import com.waggle.domain.project.service.ProjectService;
import com.waggle.domain.user.dto.UserResponseDto;
import com.waggle.domain.user.service.UserService;
import com.waggle.global.response.ApiStatus;
import com.waggle.global.response.BaseResponse;
import com.waggle.global.response.ErrorResponse;
import com.waggle.global.response.SuccessResponse;
import com.waggle.global.response.swagger.ProjectSuccessResponse;
import com.waggle.global.response.swagger.ProjectsSuccessResponse;
import com.waggle.global.secure.oauth2.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "프로젝트 지원", description = "프로젝트 지원 관련 API")
@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectApplyController {

    private final ProjectService projectService;
    private final UserService userService;

    @GetMapping("/{projectId}/applications")
    @Operation(
        summary = "프로젝트 지원자 조회",
        description = "프로젝트에 지원한 사용자들을 조회한다."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "프로젝트 지원자 조회 성공",
            content = @Content(
                schema = @Schema(implementation = ProjectSuccessResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "프로젝트 모집글이 존재하지 않습니다.",
            content = @Content(
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    public ResponseEntity<BaseResponse<List<UserResponseDto>>> fetchAppliedUsers(
        @PathVariable Long projectId
    ) {
        return SuccessResponse.of(
            ApiStatus._OK,
            projectService.getAppliedUsersByProjectId(projectId).stream()
                .map(userService::getUserInfoByUser)
                .map(UserResponseDto::from)
                .toList()
        );
    }

    @PutMapping("/{projectId}/users/{userId}/approval")
    @Operation(
        summary = "프로젝트 모집글 참여자 승인",
        description = "프로젝트 모집글에 참여한 사용자를 승인한다.",
        security = @SecurityRequirement(name = "JWT")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "프로젝트 모집글 참여자 승인 성공",
            content = @Content(
                schema = @Schema(implementation = ProjectSuccessResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자입니다.",
            content = @Content(
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "수정 권한이 없습니다.",
            content = @Content(
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "프로젝트 모집글 혹은 사용자가 존재하지 않습니다.",
            content = @Content(
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    public ResponseEntity<BaseResponse<List<UserResponseDto>>> approveUser(
        @PathVariable Long projectId,
        @PathVariable UUID userId,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return SuccessResponse.of(
            ApiStatus._OK,
            projectService.approveAppliedUser(projectId, userId, userDetails.getUser()).stream()
                .map(userService::getUserInfoByUser)
                .map(UserResponseDto::from)
                .toList()
        );
    }

    @PutMapping("/{projectId}/users/{userId}/rejection")
    @Operation(
        summary = "프로젝트 모집글 참여자 거절",
        description = "프로젝트 모집글에 참여한 사용자를 거절한다.",
        security = @SecurityRequirement(name = "JWT")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "프로젝트 모집글 참여자 거절 성공",
            content = @Content(
                schema = @Schema(implementation = ProjectSuccessResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자입니다.",
            content = @Content(
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "수정 권한이 없습니다.",
            content = @Content(
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "프로젝트 모집글 혹은 사용자가 존재하지 않습니다.",
            content = @Content(
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    public ResponseEntity<BaseResponse<List<UserResponseDto>>> rejectUser(
        @PathVariable Long projectId,
        @PathVariable UUID userId,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return SuccessResponse.of(
            ApiStatus._OK,
            projectService.rejectAppliedUser(projectId, userId, userDetails.getUser()).stream()
                .map(userService::getUserInfoByUser)
                .map(UserResponseDto::from)
                .toList()
        );
    }

    @GetMapping("/who/me")
    @Operation(
        summary = "내가 지원한 프로젝트 조회",
        description = "현재 로그인 된 사용자가 지원한 프로젝트를 조회합니다.",
        security = @SecurityRequirement(name = "JWT")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "사용자 정보 수정 성공",
            content = @Content(
                schema = @Schema(implementation = ProjectsSuccessResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자",
            content = @Content(
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    public ResponseEntity<BaseResponse<List<ProjectResponseDto>>> getAppliedProjects(
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return SuccessResponse.of(
            ApiStatus._OK,
            projectService.getAppliedProjects(userDetails.getUser()).stream()
                .map(projectService::getProjectInfoByProject)
                .map(ProjectResponseDto::from)
                .toList()
        );
    }

    @PostMapping("/{projectId}")
    @Operation(
        summary = "프로젝트 지원",
        description = "프로젝트에 지원합니다. 성공 시 지원한 프로젝트 정보를 반환합니다.",
        security = @SecurityRequirement(name = "JWT")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "프로젝트 지원 성공",
            content = @Content(
                schema = @Schema(implementation = ProjectSuccessResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자",
            content = @Content(
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "프로젝트를 찾을 수 없음",
            content = @Content(
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    public ResponseEntity<BaseResponse<ProjectResponseDto>> applyProject(
        @PathVariable Long projectId,
        @Valid @RequestBody ProjectApplicationDto projectApplicationDto,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Project project = projectService.applyProject(
            projectId,
            projectApplicationDto,
            userDetails.getUser()
        );
        ProjectInfo projectInfo = projectService.getProjectInfoByProject(project);
        return SuccessResponse.of(ApiStatus._CREATED, ProjectResponseDto.from(projectInfo));
    }

    @DeleteMapping("/{projectId}")
    @Operation(
        summary = "프로젝트 지원 취소",
        description = "프로젝트 지원을 취소합니다.",
        security = @SecurityRequirement(name = "JWT")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "프로젝트 지원 취소 성공",
            content = @Content()
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자",
            content = @Content(
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "프로젝트를 찾을 수 없음",
            content = @Content(
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    public ResponseEntity<BaseResponse<Object>> cancelApplyProject(
        @PathVariable Long projectId,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        projectService.cancelProjectApplication(projectId, userDetails.getUser());
        return SuccessResponse.of(ApiStatus._NO_CONTENT, null);
    }
}
