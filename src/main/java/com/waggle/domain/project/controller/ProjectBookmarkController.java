package com.waggle.domain.project.controller;

import com.waggle.domain.project.dto.ProjectResponseDto;
import com.waggle.domain.project.service.ProjectService;
import com.waggle.global.response.ApiStatus;
import com.waggle.global.response.BaseResponse;
import com.waggle.global.response.ErrorResponse;
import com.waggle.global.response.SuccessResponse;
import com.waggle.global.response.swagger.ProjectsSuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "프로젝트 북마크", description = "프로젝트 북마크 관련 API")
@RestController
@RequestMapping("api/v1/project/bookmark")
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
        @PathVariable UUID projectId
    ) {
        return SuccessResponse.of(
            ApiStatus._OK,
            projectService.toggleCurrentUserBookmark(projectId)
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
        ),
        @ApiResponse(
            responseCode = "404",
            description = "프로젝트를 찾을 수 없음",
            content = @Content(
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    public ResponseEntity<BaseResponse<Set<ProjectResponseDto>>> fetchMyBookmarkProjects() {
        return SuccessResponse.of(
            ApiStatus._OK,
            projectService.getCurrentUserBookmarkProjects().stream()
                .map(projectService::getProjectInfoByProject)
                .map(ProjectResponseDto::from)
                .collect(Collectors.toSet())
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
            description = "사용자 조회 성공",
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
    public ResponseEntity<BaseResponse<Set<ProjectResponseDto>>> fetchUserBookmarkProjects(
        @PathVariable UUID userId
    ) {
        return SuccessResponse.of(
            ApiStatus._OK,
            projectService.getUserBookmarkProjects(userId).stream()
                .map(projectService::getProjectInfoByProject)
                .map(ProjectResponseDto::from)
                .collect(Collectors.toCollection(LinkedHashSet::new))
        );
    }
}
