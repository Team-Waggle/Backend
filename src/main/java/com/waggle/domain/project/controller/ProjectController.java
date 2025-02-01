package com.waggle.domain.project.controller;

import com.waggle.domain.project.dto.ProjectDto;
import com.waggle.domain.project.entity.Project;
import com.waggle.domain.project.service.ProjectService;
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

import java.util.UUID;

//get: 조회, post: 생성, put: 수정, delete: 삭제
//put은 전체 다 수정, patch는 일부만 수정
@Tag(name = "프로젝트 모집", description = "프로젝트 모집 관련 API")
@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

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
    public ResponseEntity<BaseResponse<Project>> createProject(@RequestBody ProjectDto projectDto) {
        Project newProject = projectService.create(projectDto);
        return SuccessResponse.of(ApiStatus._CREATED, newProject);
    }
    @PutMapping("/post/{id}")
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
            )
    })
    public ResponseEntity<BaseResponse<Project>> updateProject(@PathVariable String id, @RequestBody ProjectDto projectDto) {
        Project updateProject = projectService.update(UUID.fromString(id), projectDto);
        return SuccessResponse.of(ApiStatus._OK, updateProject);
    }

    @DeleteMapping("/post/{id}")
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
            )
    })
    public ResponseEntity<BaseResponse<Project>> deleteProject(@PathVariable String id) {
        projectService.delete(UUID.fromString(id));
        return SuccessResponse.of(ApiStatus._NO_CONTENT, null);
    }

    @GetMapping("/post/{id}")
    @Operation(
            summary = "프로젝트 모집글 조회",
            description = "프로젝트 모집글을 조회한다."
            //security 없는 이유: 조회는 로그인 안해도 할 수 있기 때문에
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "프로젝트 모집글 조회 성공",
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
    public ResponseEntity<BaseResponse<Project>> fetchProject(@PathVariable String id) {
        Project fetchProject = projectService.findById(UUID.fromString(id));
        return SuccessResponse.of(ApiStatus._OK, fetchProject);
    }
}
