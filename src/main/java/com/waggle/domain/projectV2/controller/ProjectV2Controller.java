package com.waggle.domain.projectV2.controller;

import com.waggle.domain.application.Application;
import com.waggle.domain.application.ApplicationStatus;
import com.waggle.domain.application.dto.ApplicationResponse;
import com.waggle.domain.application.dto.CreateApplicationDto;
import com.waggle.domain.application.service.ApplicationService;
import com.waggle.domain.member.Member;
import com.waggle.domain.member.dto.MemberResponse;
import com.waggle.domain.member.service.MemberService;
import com.waggle.domain.projectV2.ProjectV2;
import com.waggle.domain.projectV2.dto.ProjectResponse;
import com.waggle.domain.projectV2.dto.UpsertProjectDto;
import com.waggle.domain.projectV2.service.ProjectV2Service;
import com.waggle.domain.recruitment.Recruitment;
import com.waggle.domain.recruitment.dto.RecruitmentResponse;
import com.waggle.domain.recruitment.service.RecruitmentService;
import com.waggle.global.response.ApiStatus;
import com.waggle.global.response.BaseResponse;
import com.waggle.global.response.SuccessResponse;
import com.waggle.global.secure.oauth2.CustomUserDetails;
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

@RequiredArgsConstructor
@RequestMapping("/api/v2/projects")
@RestController
public class ProjectV2Controller {

    private final ApplicationService applicationService;
    private final MemberService memberService;
    private final ProjectV2Service projectService;
    private final RecruitmentService recruitmentService;

    @PostMapping
    public ResponseEntity<BaseResponse<ProjectResponse>> createProject(
        @Valid @RequestBody UpsertProjectDto upsertProjectDto,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        ProjectV2 project = projectService.createProject(upsertProjectDto, userDetails.getUser());

        return SuccessResponse.of(
            ApiStatus._CREATED,
            ProjectResponse.from(project)
        );
    }


    @PostMapping("/{projectId}")
    public ResponseEntity<BaseResponse<ApplicationResponse>> applyProject(
        @PathVariable UUID projectId,
        @Valid @RequestBody CreateApplicationDto createApplicationDto,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Application application = applicationService.createApplication(
            projectId,
            createApplicationDto,
            userDetails.getUser()
        );

        return SuccessResponse.of(
            ApiStatus._CREATED,
            ApplicationResponse.from(application)
        );
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<ProjectResponse>>> getProjects() {
        List<ProjectV2> projects = projectService.getProjects();

        return SuccessResponse.of(
            ApiStatus._OK,
            projects.stream().map(ProjectResponse::from).toList()
        );
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<BaseResponse<ProjectResponse>> getProject(@PathVariable UUID projectId) {
        ProjectV2 project = projectService.getProject(projectId);

        return SuccessResponse.of(
            ApiStatus._OK,
            ProjectResponse.from(project)
        );
    }

    @GetMapping("/{projectId}/applications")
    public ResponseEntity<BaseResponse<List<ApplicationResponse>>> getProjectApplications(
        @PathVariable UUID projectId,
        @RequestParam(required = false) ApplicationStatus status,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        List<Application> applications = applicationService.getProjectApplications(
            projectId,
            status,
            userDetails.getUser()
        );

        return SuccessResponse.of(
            ApiStatus._OK,
            applications.stream().map(ApplicationResponse::from).toList()
        );
    }

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

    @GetMapping("/me")
    public ResponseEntity<BaseResponse<List<ProjectResponse>>> getMyProjects(
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        List<ProjectV2> projects = projectService.getUserProjects(userDetails.getUser().getId());

        return SuccessResponse.of(
            ApiStatus._OK,
            projects.stream().map(ProjectResponse::from).toList()
        );
    }

    @PatchMapping("/{projectId}")
    public ResponseEntity<BaseResponse<ProjectResponse>> updateProject(
        @PathVariable UUID projectId,
        @Valid @RequestBody UpsertProjectDto upsertProjectDto,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        ProjectV2 project = projectService.updateProject(
            projectId,
            upsertProjectDto,
            userDetails.getUser()
        );

        return SuccessResponse.of(
            ApiStatus._OK,
            ProjectResponse.from(project)
        );
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<BaseResponse<ProjectResponse>> deleteProject(
        @PathVariable UUID projectId,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        projectService.deleteProject(projectId, userDetails.getUser());

        return SuccessResponse.of(
            ApiStatus._NO_CONTENT,
            null
        );
    }
}
