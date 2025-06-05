package com.waggle.domain.project.controller;

import com.waggle.domain.project.ProjectInfo;
import com.waggle.domain.project.dto.ProjectResponseDto;
import com.waggle.domain.project.entity.Project;
import com.waggle.domain.project.service.ProjectService;
import com.waggle.global.response.ApiStatus;
import com.waggle.global.response.BaseResponse;
import com.waggle.global.response.CursorResponse;
import com.waggle.global.response.ErrorResponse;
import com.waggle.global.response.SuccessResponse;
import com.waggle.global.response.swagger.ProjectsSuccessResponse;
import com.waggle.global.secure.oauth2.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "프로젝트 북마크", description = "프로젝트 북마크 관련 API")
@RestController
@RequestMapping("/api/v1/projects/bookmark")
@RequiredArgsConstructor
public class ProjectBookmarkController {

    private final ProjectService projectService;

    @PostMapping(value = "/{projectId}")
    @Operation(
        summary = "프로젝트 북마크 추가/제거",
        description = """
            프로젝트가 북마크 되어있지 않다면 북마크를 추가하고, 북마크 되어있다면 북마크를 제거합니다.
            
            ⚠️ 프로젝트가 이미 북마크 되어있으면 북마크를 해제하고 payload는 false를 return합니다.\n
            payload의 return이 false인 것이 호출 실패를 의미하진 않습니다.
            """,
        security = @SecurityRequirement(name = "JWT")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "사용자 정보 수정 성공",
            content = @Content(
                schema = @Schema(implementation = SuccessResponse.class)
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
    public ResponseEntity<BaseResponse<Boolean>> toggleMyBookmark(
        @PathVariable Long projectId,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return SuccessResponse.of(
            ApiStatus._OK,
            projectService.toggleCurrentUserBookmark(projectId, userDetails.getUser())
        );
    }

    @GetMapping(value = "/who/me")
    @Operation(
        summary = "북마크한 프로젝트 모집글 조회",
        description = "사용자가 북마크한 프로젝트 모집글을 조회합니다.",
        security = @SecurityRequirement(name = "JWT")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "북마크 목록 조회 성공",
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
    public ResponseEntity<BaseResponse<CursorResponse<ProjectResponseDto>>> fetchMyBookmarkProjects(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestParam(required = false) Long cursor,
        @RequestParam(defaultValue = "10") int size
    ) {
        List<Project> projects = projectService.getUserBookmarkProjects(
            userDetails.getUser().getId(), cursor, size);

        return SuccessResponse.of(
            ApiStatus._OK,
            CursorResponse.of(
                projects,
                size,
                project -> {
                    ProjectInfo projectInfo = projectService.getProjectInfoByProject(project);
                    return ProjectResponseDto.from(projectInfo);
                },
                Project::getId
            )
        );
    }

    @GetMapping("/who/{userId}")
    @Operation(
        summary = "특정 사용자가 북마크한 프로젝트 모집글 조회",
        description = "특정 사용자가 북마크한 프로젝트 모집글을 조회합니다."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "북마크 목록 조회 성공",
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
        ),
        @ApiResponse(
            responseCode = "404",
            description = "사용자를 찾을 수 없음",
            content = @Content(
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    public ResponseEntity<BaseResponse<CursorResponse<ProjectResponseDto>>> fetchUserBookmarkProjects(
        @PathVariable UUID userId,
        @RequestParam(required = false) Long cursor,
        @RequestParam(defaultValue = "10") int size
    ) {
        List<Project> projects = projectService.getUserBookmarkProjects(userId, cursor, size);

        return SuccessResponse.of(
            ApiStatus._OK,
            CursorResponse.of(
                projects,
                size,
                project -> {
                    ProjectInfo projectInfo = projectService.getProjectInfoByProject(project);
                    return ProjectResponseDto.from(projectInfo);
                },
                Project::getId
            )
        );
    }
}
