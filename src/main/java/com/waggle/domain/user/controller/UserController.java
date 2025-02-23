package com.waggle.domain.user.controller;

import com.waggle.domain.project.dto.ProjectResponseDto;
import com.waggle.domain.project.entity.Project;
import com.waggle.domain.user.dto.UserInputDto;
import com.waggle.domain.user.dto.UserResponseDto;
import com.waggle.domain.user.entity.User;
import com.waggle.domain.user.service.UserService;
import com.waggle.global.response.ApiStatus;
import com.waggle.global.response.BaseResponse;
import com.waggle.global.response.ErrorResponse;
import com.waggle.global.response.SuccessResponse;
import com.waggle.global.response.swagger.ProjectSuccessResponse;
import com.waggle.global.response.swagger.ProjectsSuccessResponse;
import com.waggle.global.response.swagger.UserSuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Tag(name = "사용자", description = "사용자 관련 API")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @Operation(
            summary = "현재 사용자 조회",
            description = "현재 로그인 된 사용자를 조회합니다.",
            security = @SecurityRequirement(name = "JWT")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "사용자 조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = UserSuccessResponse.class)
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
    public ResponseEntity<BaseResponse<UserResponseDto>> fetchMe() {
        User currentUserUser = userService.getCurrentUser();
        return SuccessResponse.of(ApiStatus._OK, UserResponseDto.from(currentUserUser));
    }

    @PutMapping(value = "/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "현재 사용자 정보 수정",
            description = "현재 로그인 된 사용자의 정보를 수정합니다.",
            security = @SecurityRequirement(name = "JWT")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "사용자 정보 수정 성공",
                    content = @Content(
                            schema = @Schema(implementation = UserSuccessResponse.class)
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
    public ResponseEntity<BaseResponse<UserResponseDto>> updateMe(
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
            @RequestPart(value = "updateUserDto") UserInputDto userInputDto
    ) {
        User updatedUser = userService.updateCurrentUser(profileImage, userInputDto);
        return SuccessResponse.of(ApiStatus._OK, UserResponseDto.from(updatedUser));
    }

    @DeleteMapping("/me")
    @Operation(
            summary = "현재 사용자 삭제",
            description = "현재 로그인 된 사용자를 삭제합니다.",
            security = @SecurityRequirement(name = "JWT")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "사용자 삭제 성공", content = @Content()),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<BaseResponse<Object>> deleteMe() {
        userService.deleteCurrentUser();
        return SuccessResponse.of(ApiStatus._NO_CONTENT, null);
    }

    @GetMapping("/me/project")
    @Operation(
            summary = "내가 작성한 프로젝트 모집글 조회",
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
    public ResponseEntity<BaseResponse<Set<ProjectResponseDto>>> fetchMyProjects() {
        return SuccessResponse.of(ApiStatus._OK, userService.getCurrentUserProjects().stream()
                .map(ProjectResponseDto::from)
                .collect(Collectors.toCollection(LinkedHashSet::new)));
    }

    @DeleteMapping("/me/project/{projectId}")
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
    public ResponseEntity<BaseResponse<Object>> quitMyProject(@PathVariable String projectId) {
        userService.deleteUserProject(projectId);
        return SuccessResponse.of(ApiStatus._NO_CONTENT, null);
    }

    @PostMapping(value = "/me/project/bookmark")
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
            @RequestParam(value = "projectId") String projectId
    ) {
        boolean isBookmarked = userService.toggleCurrentUserBookmark(projectId);
        return SuccessResponse.of(ApiStatus._OK, isBookmarked);
    }

    @GetMapping(value = "/me/project/bookmark")
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
        Set<ProjectResponseDto> projectResponseDtos = userService.getCurrentUserBookmarkProjects().stream()
                .map(ProjectResponseDto::from)
                .collect(Collectors.toSet());
        return SuccessResponse.of(ApiStatus._OK, projectResponseDtos);
    }

    @GetMapping("/{userId}")
    @Operation(
            summary = "특정 사용자 조회",
            description = "특정 사용자를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "사용자 조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = UserSuccessResponse.class)
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
    public ResponseEntity<BaseResponse<UserResponseDto>> fetchUser(@PathVariable String userId) {
        User user = userService.getUserByUserId(userId);
        return SuccessResponse.of(ApiStatus._OK, UserResponseDto.from(user));
    }

    @GetMapping("/{userId}/project")
    @Operation(
            summary = "특정 사용자가 작성한 프로젝트 모집글 조회",
            description = "특정 사용자가 작성한 프로젝트 모집글을 조회합니다."
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
    public ResponseEntity<BaseResponse<Set<ProjectResponseDto>>> fetchUserProjects(@PathVariable String userId) {
        Set<ProjectResponseDto> projectResponseDtos = userService.getUserProjects(userId).stream()
                .map(ProjectResponseDto::from)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return SuccessResponse.of(ApiStatus._OK, projectResponseDtos);
    }

    @GetMapping("/{userId}/project/bookmark")
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
    public ResponseEntity<BaseResponse<Set<ProjectResponseDto>>> fetchUserBookmarkProjects(@PathVariable String userId) {
        Set<ProjectResponseDto> projectResponseDtos = userService.getUserBookmarkProjects(userId).stream()
                .map(ProjectResponseDto::from)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return SuccessResponse.of(ApiStatus._OK, projectResponseDtos);
    }

    @GetMapping("/me/project/apply")
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
    public ResponseEntity<BaseResponse<Set<ProjectResponseDto>>> getAppliedProjects() {
        Set<ProjectResponseDto> appliedProjects = userService.getAppliedProjects().stream()
                .map(ProjectResponseDto::from)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return SuccessResponse.of(ApiStatus._OK, appliedProjects);
    }

    @PostMapping("/me/project/apply")
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
    public ResponseEntity<BaseResponse<ProjectResponseDto>> applyProject(@RequestParam String projectId) {
        ProjectResponseDto applyProject = ProjectResponseDto.from(userService.applyProject(projectId));
        return SuccessResponse.of(ApiStatus._CREATED, applyProject);
    }

    @DeleteMapping("/me/project/apply")
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
    public ResponseEntity<BaseResponse<Object>> cancelApplyProject(@RequestParam String projectId) {
        userService.cancelApplyProject(projectId);
        return SuccessResponse.of(ApiStatus._NO_CONTENT, null);
    }
}