package com.waggle.domain.user.controller;

import com.waggle.domain.project.dto.ProjectResponseDto;
import com.waggle.domain.project.entity.Project;
import com.waggle.domain.user.dto.UserInputDto;
import com.waggle.domain.user.dto.UserWithProjectResponseDto;
import com.waggle.domain.user.entity.User;
import com.waggle.domain.user.service.UserService;
import com.waggle.global.response.ApiStatus;
import com.waggle.global.response.BaseResponse;
import com.waggle.global.response.ErrorResponse;
import com.waggle.global.response.SuccessResponse;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
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
    public ResponseEntity<BaseResponse<UserWithProjectResponseDto>> fetchCurrentUser() {
        User currentUserUser = userService.getCurrentUser();
        return SuccessResponse.of(ApiStatus._OK, UserWithProjectResponseDto.from(currentUserUser));
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
    public ResponseEntity<BaseResponse<UserWithProjectResponseDto>> updateUser(
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
            @RequestPart(value = "updateUserDto") UserInputDto userInputDto
    ) {
        User updatedUser = userService.updateUser(profileImage, userInputDto);
        return SuccessResponse.of(ApiStatus._OK, UserWithProjectResponseDto.from(updatedUser));
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
    public ResponseEntity<BaseResponse<Object>> deleteUser() {
        userService.deleteUser();
        return SuccessResponse.of(ApiStatus._NO_CONTENT, null);
    }

    @PostMapping(value = "/project/bookmark")
    @Operation(
            summary = "프로젝트 북마크 토글",
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
    public ResponseEntity<BaseResponse<Boolean>> toggleBookmark(
            @RequestParam(value = "projectId") String projectId
    ) {
        boolean isBookmarked = userService.toggleBookmark(projectId);
        return SuccessResponse.of(ApiStatus._OK, isBookmarked);
    }

    @GetMapping(value = "/project/bookmark")
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
    public ResponseEntity<BaseResponse<Set<ProjectResponseDto>>> fetchBookmarkProjects() {
        Set<ProjectResponseDto> projectResponseDtos = userService.getBookmarkProjects().stream()
                .map(ProjectResponseDto::from)
                .collect(Collectors.toSet());
        return SuccessResponse.of(ApiStatus._OK, projectResponseDtos);
    }
}