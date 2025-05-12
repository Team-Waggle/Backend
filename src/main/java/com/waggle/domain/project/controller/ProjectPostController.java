package com.waggle.domain.project.controller;

import static org.springframework.data.domain.Sort.Direction.DESC;

import com.waggle.domain.project.dto.ProjectInputDto;
import com.waggle.domain.project.dto.ProjectResponseDto;
import com.waggle.domain.project.entity.Project;
import com.waggle.domain.project.service.ProjectService;
import com.waggle.global.response.ApiStatus;
import com.waggle.global.response.BaseResponse;
import com.waggle.global.response.ErrorResponse;
import com.waggle.global.response.SuccessResponse;
import com.waggle.global.response.swagger.ProjectSuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//get: 조회, post: 생성, put: 수정, delete: 삭제
//put은 전체 다 수정, patch는 일부만 수정
@Tag(name = "프로젝트 모집 게시글", description = "프로젝트 모집 게시글 관련 API")
@RestController
@RequestMapping("/project/post")
@RequiredArgsConstructor
public class ProjectPostController {

    private final ProjectService projectService;

    @GetMapping
    @Operation(
        summary = "프로젝트 모집글 목록 조회",
        description = "프로젝트 모집글 목록을 페이지네이션으로 조회한다."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "프로젝트 모집글 목록 조회 성공",
            content = @Content(
                schema = @Schema(implementation = ProjectSuccessResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청입니다.",
            content = @Content(
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    public ResponseEntity<BaseResponse<Page<ProjectResponseDto>>> fetchProjects(
        @PageableDefault(size = 10, sort = "createdAt", direction = DESC) Pageable pageable
    ) {
        return SuccessResponse.of(
            ApiStatus._OK,
            projectService.getProjects(pageable).map(ProjectResponseDto::from)
        );
    }

    @GetMapping("/{projectId}")
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
    public ResponseEntity<BaseResponse<ProjectResponseDto>> fetchProject(
        @PathVariable String projectId) {
        Project fetchProject = projectService.getProjectByProjectId(UUID.fromString(projectId));
        return SuccessResponse.of(ApiStatus._OK, ProjectResponseDto.from(fetchProject));
    }

    @PostMapping("") //경로에 있는 post는 post 방식이 아니라 게시글을 영어로 한거임
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
    public ResponseEntity<BaseResponse<ProjectResponseDto>> createProject(
        @RequestBody ProjectInputDto projectInputDto) {
        Project newProject = projectService.createProject(projectInputDto);
        return SuccessResponse.of(ApiStatus._CREATED, ProjectResponseDto.from(newProject));
    }

    @PutMapping("/{projectId}")
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
    public ResponseEntity<BaseResponse<ProjectResponseDto>> updateProject(
        @PathVariable String projectId, @RequestBody ProjectInputDto projectInputDto) {
        Project updateProject = projectService.updateProject(UUID.fromString(projectId),
            projectInputDto);
        return SuccessResponse.of(ApiStatus._OK, ProjectResponseDto.from(updateProject));
    }

    @DeleteMapping("/{projectId}")
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
}
