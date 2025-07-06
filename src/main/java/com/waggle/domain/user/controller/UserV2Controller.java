package com.waggle.domain.user.controller;

import com.waggle.domain.application.Application;
import com.waggle.domain.application.ApplicationStatus;
import com.waggle.domain.application.dto.ApplicationResponse;
import com.waggle.domain.application.service.ApplicationService;
import com.waggle.domain.bookmark.Bookmarkable;
import com.waggle.domain.bookmark.dto.BookmarkResponse;
import com.waggle.domain.bookmark.service.BookmarkService;
import com.waggle.domain.projectV2.ProjectV2;
import com.waggle.domain.projectV2.dto.ProjectResponse;
import com.waggle.domain.projectV2.service.ProjectV2Service;
import com.waggle.domain.user.service.UserService;
import com.waggle.global.response.ApiStatus;
import com.waggle.global.response.BaseResponse;
import com.waggle.global.response.ErrorResponse;
import com.waggle.global.response.SuccessResponse;
import com.waggle.global.response.swagger.ApplicationsSuccessResponse;
import com.waggle.global.response.swagger.BookmarksSuccessResponse;
import com.waggle.global.response.swagger.ProjectV2sSuccessResponse;
import com.waggle.global.security.oauth2.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v2/users/me")
@RestController
public class UserV2Controller {

    private final ApplicationService applicationService;
    private final BookmarkService bookmarkService;
    private final ProjectV2Service projectService;
    private final UserService userService;

    @Operation(
        summary = "내 지원 목록 조회",
        description = "인증된 사용자의 지원 목록을 조회합니다."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "지원 목록 조회 성공",
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
        )
    })
    @GetMapping("/applications")
    public ResponseEntity<BaseResponse<List<ApplicationResponse>>> getMyApplications(
        @RequestParam(required = false) ApplicationStatus status,
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        List<Application> applications = applicationService.getUserApplications(
            status,
            userPrincipal.getUser()
        );

        return SuccessResponse.of(
            ApiStatus._OK,
            applications.stream().map(ApplicationResponse::from).toList()
        );
    }

    @Operation(
        summary = "내 북마크 목록 조회",
        description = "인증된 사용자의 북마크 목록을 조회합니다."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "북마크 목록 조회 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BookmarksSuccessResponse.class)
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
    @GetMapping("/bookmarks")
    public ResponseEntity<BaseResponse<List<BookmarkResponse>>> getMyBookmarks(
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        List<Bookmarkable> bookmarkables = bookmarkService.getUserBookmarks(
            userPrincipal.getUser());

        return SuccessResponse.of(
            ApiStatus._OK,
            bookmarkables.stream().map(BookmarkResponse::from).toList()
        );
    }

    @Operation(
        summary = "프로젝트 목록 조회",
        description = "인증된 사용자의 프로젝트 목록을 조회합니다."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "프로젝트 목록 조회 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProjectV2sSuccessResponse.class)
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
    @GetMapping("/projects")
    public ResponseEntity<BaseResponse<List<ProjectResponse>>> getMyProjects(
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        List<ProjectV2> projects = projectService.getUserProjects(userPrincipal.getUser().getId());

        return SuccessResponse.of(
            ApiStatus._OK,
            projects.stream().map(ProjectResponse::from).toList()
        );
    }
}
