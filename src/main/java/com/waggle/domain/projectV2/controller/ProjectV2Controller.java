package com.waggle.domain.projectV2.controller;

import com.waggle.domain.application.Application;
import com.waggle.domain.application.ApplicationStatus;
import com.waggle.domain.application.dto.ApplicationResponse;
import com.waggle.domain.application.dto.CreateApplicationRequest;
import com.waggle.domain.application.service.ApplicationService;
import com.waggle.domain.member.Member;
import com.waggle.domain.member.dto.MemberResponse;
import com.waggle.domain.member.service.MemberService;
import com.waggle.domain.projectV2.ProjectV2;
import com.waggle.domain.projectV2.dto.ProjectResponse;
import com.waggle.domain.projectV2.dto.UpsertProjectRequest;
import com.waggle.domain.projectV2.service.ProjectV2Service;
import com.waggle.domain.recruitment.Recruitment;
import com.waggle.domain.recruitment.dto.RecruitmentResponse;
import com.waggle.domain.recruitment.service.RecruitmentService;
import com.waggle.global.response.ApiStatus;
import com.waggle.global.response.BaseResponse;
import com.waggle.global.response.ErrorResponse;
import com.waggle.global.response.SuccessResponse;
import com.waggle.global.response.swagger.ApplicationSuccessResponse;
import com.waggle.global.response.swagger.ApplicationsSuccessResponse;
import com.waggle.global.response.swagger.EmptySuccessResponse;
import com.waggle.global.response.swagger.MembersSuccessResponse;
import com.waggle.global.response.swagger.ProjectV2SuccessResponse;
import com.waggle.global.response.swagger.ProjectV2sSuccessResponse;
import com.waggle.global.response.swagger.RecruitmentsSuccessResponse;
import com.waggle.global.security.oauth2.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ProjectV2", description = "프로젝트 관리 API V2")
@RequiredArgsConstructor
@RequestMapping("/v2/projects")
@RestController
public class ProjectV2Controller {

    private final ApplicationService applicationService;
    private final MemberService memberService;
    private final ProjectV2Service projectService;
    private final RecruitmentService recruitmentService;

    @Operation(
        summary = "프로젝트 생성",
        description = "프로젝트를 생섭합니다."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "프로젝트 생성 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProjectV2SuccessResponse.class)
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
    @PostMapping
    public ResponseEntity<BaseResponse<ProjectResponse>> createProject(
        @Valid @RequestBody UpsertProjectRequest upsertProjectRequest,
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        ProjectV2 project = projectService.createProject(upsertProjectRequest,
            userPrincipal.getUser());

        return SuccessResponse.of(
            ApiStatus._CREATED,
            ProjectResponse.from(project)
        );
    }


    @Operation(
        summary = "프로젝트 지원",
        description = "프로젝트에 지원합니다."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "프로젝트 지원 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApplicationSuccessResponse.class)
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
            responseCode = "404",
            description = "존재하지 않는 프로젝트",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    @PostMapping("/{projectId}")
    public ResponseEntity<BaseResponse<ApplicationResponse>> applyProject(
        @PathVariable UUID projectId,
        @Valid @RequestBody CreateApplicationRequest createApplicationRequest,
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        Application application = applicationService.createApplication(
            projectId,
            createApplicationRequest,
            userPrincipal.getUser()
        );

        return SuccessResponse.of(
            ApiStatus._CREATED,
            ApplicationResponse.from(application)
        );
    }

    @Operation(
        summary = "프로젝트 전체 목록 조회",
        description = "프로젝트의 전체 목록을 조회합니다."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "프로젝트 목록 조회 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProjectV2sSuccessResponse.class)
            )
        )
    })
    @GetMapping
    public ResponseEntity<BaseResponse<List<ProjectResponse>>> getProjects() {
        List<ProjectV2> projects = projectService.getProjects();

        return SuccessResponse.of(
            ApiStatus._OK,
            projects.stream().map(ProjectResponse::from).toList()
        );
    }

    @Operation(
        summary = "프로젝트 조회",
        description = "프로젝트를 조회합니다."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "프로젝트 조회 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProjectV2SuccessResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "존재하지 않는 프로젝트",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    @GetMapping("/{projectId}")
    public ResponseEntity<BaseResponse<ProjectResponse>> getProject(@PathVariable UUID projectId) {
        ProjectV2 project = projectService.getProject(projectId);

        return SuccessResponse.of(
            ApiStatus._OK,
            ProjectResponse.from(project)
        );
    }

    @Operation(
        summary = "프로젝트 지원 목록 조회",
        description = "프로젝트의 지원 목록을 조회합니다."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "프로젝트 지원 목록 조회 성공",
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
        ),
        @ApiResponse(
            responseCode = "403",
            description = "사용자 멤버 접근 제한",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
    })
    @GetMapping("/{projectId}/applications")
    public ResponseEntity<BaseResponse<List<ApplicationResponse>>> getProjectApplications(
        @PathVariable UUID projectId,
        @RequestParam(required = false) ApplicationStatus status,
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        List<Application> applications = applicationService.getProjectApplications(
            projectId,
            status,
            userPrincipal.getUser()
        );

        return SuccessResponse.of(
            ApiStatus._OK,
            applications.stream().map(ApplicationResponse::from).toList()
        );
    }

    @Operation(
        summary = "프로젝트 멤버 목록 조회",
        description = "프로젝트의 멤버 목록을 조회합니다."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "프로젝트 멤버 목록 조회 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = MembersSuccessResponse.class)
            )
        )
    })
    @GetMapping("/{projectId}/members")
    public ResponseEntity<BaseResponse<List<MemberResponse>>> getProjectMembers(
        @PathVariable UUID projectId
    ) {
        List<Member> members = memberService.getProjectMembers(projectId);

        return SuccessResponse.of(
            ApiStatus._OK,
            members.stream().map(MemberResponse::from).toList()
        );
    }

    @Operation(
        summary = "프로젝트 모집 목록 조회",
        description = "프로젝트의 모집 목록을 조회합니다."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "프로젝트 모집 목록 조회 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RecruitmentsSuccessResponse.class)
            )
        )
    })
    @GetMapping("/{projectId}/recruitments")
    public ResponseEntity<BaseResponse<List<RecruitmentResponse>>> getProjectRecruitments(
        @PathVariable UUID projectId
    ) {
        List<Recruitment> recruitments = recruitmentService.getProjectRecruitments(projectId);

        return SuccessResponse.of(
            ApiStatus._OK,
            recruitments.stream().map(RecruitmentResponse::from).toList()
        );
    }

    @Operation(
        summary = "프로젝트 수정",
        description = "프로젝트를 수정합니다."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "프로젝트 수정 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProjectV2SuccessResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "중복된 프로젝트 이름",
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
            description = "사용자 프로젝트 접근 제한",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "존재하지 않는 프로젝트",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    @PatchMapping("/{projectId}")
    public ResponseEntity<BaseResponse<ProjectResponse>> updateProject(
        @PathVariable UUID projectId,
        @Valid @RequestBody UpsertProjectRequest upsertProjectRequest,
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        ProjectV2 project = projectService.updateProject(
            projectId,
            upsertProjectRequest,
            userPrincipal.getUser()
        );

        return SuccessResponse.of(
            ApiStatus._OK,
            ProjectResponse.from(project)
        );
    }

    @Operation(
        summary = "프로젝트 삭제",
        description = "프로젝트를 삭제합니다."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "프로젝트 삭제 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EmptySuccessResponse.class)
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
            responseCode = "404",
            description = "존재하지 않는 프로젝트",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    @DeleteMapping("/{projectId}")
    public ResponseEntity<BaseResponse<ProjectResponse>> deleteProject(
        @PathVariable UUID projectId,
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        projectService.deleteProject(projectId, userPrincipal.getUser());

        return SuccessResponse.of(
            ApiStatus._NO_CONTENT,
            null
        );
    }
}
