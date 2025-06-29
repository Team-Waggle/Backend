package com.waggle.domain.project.controller;

import com.waggle.domain.project.dto.ProjectResponseDto;
import com.waggle.domain.project.service.ProjectService;
import com.waggle.domain.user.dto.UserResponseDto;
import com.waggle.domain.user.service.UserService;
import com.waggle.global.response.ApiStatus;
import com.waggle.global.response.BaseResponse;
import com.waggle.global.response.ErrorResponse;
import com.waggle.global.response.SuccessResponse;
import com.waggle.global.response.swagger.ProjectSuccessResponse;
import com.waggle.global.response.swagger.ProjectsSuccessResponse;
import com.waggle.global.security.oauth2.UserPrincipal;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "프로젝트 멤버", description = "프로젝트 멤버 관련 API")
@RestController
@RequestMapping("/api/v1/projects/member")
@RequiredArgsConstructor
public class ProjectMemberController {

    private final ProjectService projectService;
    private final UserService userService;

    @GetMapping("/{projectId}")
    @Operation(
        summary = "프로젝트 모집글 참여자 조회",
        description = "프로젝트 모집글에 참여한 사용자들을 조회한다."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "프로젝트 모집글 참여자 조회 성공",
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
    public ResponseEntity<BaseResponse<List<UserResponseDto>>> fetchUsers(
        @PathVariable Long projectId
    ) {
        // TODO: 정렬 기준 재고
        return SuccessResponse.of(
            ApiStatus._OK,
            projectService.getUsersByProjectId(projectId).stream()
                .map(userService::getUserInfoByUser)
                .map(UserResponseDto::from)
//                .sorted(Comparator
//                    .comparing((UserResponseDto dto) -> {
//                        // Position 이름으로 먼저 정렬 (첫 번째 Position 기준 - 없으면 빈 문자열)
//                        Set<UserPositionDto> positions = dto.userPositionDtos();
//                        return positions != null && !positions.isEmpty() ? positions.get(0) : "";
//                    })
//                    .thenComparing(UserResponseDto::name))
                .toList()
        );
    }

    @PutMapping("/{projectId}/reject/{userId}")
    @Operation(
        summary = "프로젝트 모집글 참여자 강제 퇴장",
        description = "프로젝트 모집글에 참여한 사용자를 강제 퇴장시킨다.",
        security = @SecurityRequirement(name = "JWT")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "프로젝트 모집글 참여자 제명 성공",
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
    public ResponseEntity<BaseResponse<List<UserResponseDto>>> removeMember(
        @PathVariable Long projectId,
        @PathVariable UUID userId,
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return SuccessResponse.of(
            ApiStatus._OK,
            projectService.removeMemberUser(projectId, userId, userPrincipal.getUser()).stream()
                .map(userService::getUserInfoByUser)
                .map(UserResponseDto::from)
                .toList()
        );
    }

    @PutMapping("/{projectId}/delegate/{userId}")
    @Operation(
        summary = "프로젝트 리더 위임",
        description = "프로젝트 리더를 위임한다.",
        security = @SecurityRequirement(name = "JWT")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "프로젝트 리더 위임 성공",
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
    public ResponseEntity<BaseResponse<List<UserResponseDto>>> delegateLeader(
        @PathVariable Long projectId,
        @PathVariable UUID userId,
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        projectService.delegateLeader(projectId, userId, userPrincipal.getUser());
        return SuccessResponse.of(ApiStatus._OK, null);
    }

    @DeleteMapping("/{projectId}")
    @Operation(
        summary = "내가 참여한 프로젝트 탈퇴",
        description = "현재 로그인 된 사용자가 참여한 프로젝트를 삭제합니다.",
        security = @SecurityRequirement(name = "JWT")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "프로젝트 탈퇴 성공",
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
            responseCode = "403",
            description = "삭제 권한이 없습니다.",
            content = @Content(
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    public ResponseEntity<BaseResponse<Object>> quitMyProject(
        @PathVariable Long projectId,
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        projectService.withdrawFromProject(projectId, userPrincipal.getUser());
        return SuccessResponse.of(ApiStatus._NO_CONTENT, null);
    }

    @GetMapping("/who/me")
    @Operation(
        summary = "내가 참가한 프로젝트의 모집글 조회",
        description = "현재 로그인 된 사용자가 작성한 프로젝트 모집글을 조회합니다.",
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
    public ResponseEntity<BaseResponse<List<ProjectResponseDto>>> fetchMyProjects(
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return SuccessResponse.of(
            ApiStatus._OK,
            projectService.getCurrentUserProjects(userPrincipal.getUser()).stream()
                .map(projectService::getProjectInfoByProject)
                .map(ProjectResponseDto::from)
                .toList()
        );
    }

    @GetMapping("/who/{userId}")
    @Operation(
        summary = "특정 사용자가 참가한 프로젝트 모집글 조회",
        description = "특정 사용자가 참가한 프로젝트 모집글을 조회합니다."
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
    public ResponseEntity<BaseResponse<List<ProjectResponseDto>>> fetchUserProjects(
        @PathVariable UUID userId
    ) {

        return SuccessResponse.of(
            ApiStatus._OK,
            projectService.getUserProjects(userId).stream()
                .map(projectService::getProjectInfoByProject)
                .map(ProjectResponseDto::from)
                .toList()
        );
    }
}
