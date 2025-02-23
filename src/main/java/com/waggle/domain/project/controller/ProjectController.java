package com.waggle.domain.project.controller;

import com.waggle.domain.project.dto.ProjectInputDto;
import com.waggle.domain.project.dto.ProjectResponseDto;
import com.waggle.domain.project.entity.Project;
import com.waggle.domain.project.service.ProjectService;
import com.waggle.domain.user.dto.UserResponseDto;
import com.waggle.global.response.*;
import com.waggle.global.response.swagger.ProjectSuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

//get: 조회, post: 생성, put: 수정, delete: 삭제
//put은 전체 다 수정, patch는 일부만 수정
@Tag(name = "프로젝트 모집", description = "프로젝트 모집 관련 API")
@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/{projectId}/post")
    @Operation(
            summary = "프로젝트 모집글 조회",
            description = "프로젝트 모집글을 조회한다."
            //security 없는 이유: 조회는 로그인 안해도 할 수 있기 때문에
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "프로젝트 모집글 조회 성공",
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
    public ResponseEntity<BaseResponse<ProjectResponseDto>> fetchProject(@PathVariable String projectId) {
        Project fetchProject = projectService.getProjectByProjectId(UUID.fromString(projectId));
        return SuccessResponse.of(ApiStatus._OK, ProjectResponseDto.from(fetchProject));
    }

    @PostMapping("/post") //경로에 있는 post는 post 방식이 아니라 게시글을 영어로 한거임
    //이 경로는 /project/post
    @Operation(
            summary = "프로젝트 모집글 생성",
            description = "프로젝트 모집글을 생성한다.",
            security = @SecurityRequirement(name = "JWT")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "프로젝트 모집글 생성 성공",
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
            )
    })
    public ResponseEntity<BaseResponse<ProjectResponseDto>> createProject(@RequestBody ProjectInputDto projectInputDto) {
        Project newProject = projectService.createProject(projectInputDto);
        return SuccessResponse.of(ApiStatus._CREATED, ProjectResponseDto.from(newProject));
    }
    @PutMapping("/{projectId}/post")
    @Operation(
            summary = "프로젝트 모집글 수정",
            description = "프로젝트 모집글을 수정한다.",
            security = @SecurityRequirement(name = "JWT")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "프로젝트 모집글 수정 성공",
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
                    description = "프로젝트 모집글이 존재하지 않습니다.",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<BaseResponse<ProjectResponseDto>> updateProject(@PathVariable String projectId, @RequestBody ProjectInputDto projectInputDto) {
        Project updateProject = projectService.updateProject(UUID.fromString(projectId), projectInputDto);
        return SuccessResponse.of(ApiStatus._OK, ProjectResponseDto.from(updateProject));
    }

    @DeleteMapping("/{projectId}/post")
    @Operation(
            summary = "프로젝트 모집글 삭제",
            description = "프로젝트 모집글을 삭제한다.",
            security = @SecurityRequirement(name = "JWT")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "프로젝트 모집글 삭제 성공",
                    content = @Content()
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
                    description = "삭제 권한이 없습니다.",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
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
    public ResponseEntity<BaseResponse<Object>> deleteProject(@PathVariable String projectId) {
        projectService.deleteProject(UUID.fromString(projectId));
        return SuccessResponse.of(ApiStatus._NO_CONTENT, null);
    }

    @GetMapping("/{projectId}/member")
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
    public ResponseEntity<BaseResponse<Set<UserResponseDto>>> fetchUsers(@PathVariable String projectId) {
        return SuccessResponse.of(ApiStatus._OK, projectService.getUsersByProjectId(UUID.fromString(projectId)).stream()
                .map(UserResponseDto::from)
                .collect(Collectors.toCollection(LinkedHashSet::new)));
    }

    @PutMapping("/{projectId}/member/{userId}/reject")
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
    public ResponseEntity<BaseResponse<Set<UserResponseDto>>> rejectMember(@PathVariable String projectId, @PathVariable String userId) {
        return SuccessResponse.of(ApiStatus._OK, projectService.rejectMemberUser(UUID.fromString(projectId), userId).stream()
                .map(UserResponseDto::from)
                .collect(Collectors.toCollection(LinkedHashSet::new)));
    }

    @PutMapping("/{projectId}/member/{userId}/delegate")
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
    public ResponseEntity<BaseResponse<Set<UserResponseDto>>> delegateLeader(@PathVariable String projectId, @PathVariable String userId) {
        projectService.delegateLeader(UUID.fromString(projectId), userId);
        return SuccessResponse.of(ApiStatus._OK, null);
    }

    @PutMapping("/{projectId}/apply/{userId}/approve")
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
    public ResponseEntity<BaseResponse<Set<UserResponseDto>>> approveUser(@PathVariable String projectId, @PathVariable String userId) {
        return SuccessResponse.of(ApiStatus._OK, projectService.approveAppliedUser(UUID.fromString(projectId), userId).stream()
                .map(UserResponseDto::from)
                .collect(Collectors.toCollection(LinkedHashSet::new)));
    }

    @PutMapping("/{projectId}/apply/{userId}/reject")
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
    public ResponseEntity<BaseResponse<Set<UserResponseDto>>> rejectUser(@PathVariable String projectId, @PathVariable String userId) {
        return SuccessResponse.of(ApiStatus._OK, projectService.rejectAppliedUser(UUID.fromString(projectId), userId).stream()
                .map(UserResponseDto::from)
                .collect(Collectors.toCollection(LinkedHashSet::new)));
    }
}
