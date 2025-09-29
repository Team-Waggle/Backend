package com.waggle.domain.user.controller;

import com.waggle.domain.follow.service.FollowService;
import com.waggle.domain.project.dto.ProjectResponseDto;
import com.waggle.domain.project.service.ProjectService;
import com.waggle.domain.user.SimpleUserInfo;
import com.waggle.domain.user.UserInfo;
import com.waggle.domain.user.dto.UserInputDto;
import com.waggle.domain.user.dto.UserResponseDto;
import com.waggle.domain.user.entity.User;
import com.waggle.domain.user.service.UserService;
import com.waggle.global.response.ApiStatus;
import com.waggle.global.response.BaseResponse;
import com.waggle.global.response.ErrorResponse;
import com.waggle.global.response.SuccessResponse;
import com.waggle.global.response.swagger.ProjectSuccessResponse;
import com.waggle.global.response.swagger.UserDtoSuccessResponse;
import com.waggle.global.security.oauth2.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "사용자", description = "사용자 관련 API")
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final FollowService followService;
    private final ProjectService projectService;
    private final UserService userService;

    @PostMapping(value = "/me/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
        summary = "프로필 이미지 업로드",
        description = "현재 사용자의 프로필 이미지를 업로드합니다.",
        security = @SecurityRequirement(name = "JWT")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "프로필 이미지 업로드 성공",
            content = @Content(
                schema = @Schema(implementation = UserDtoSuccessResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "잘못된 파일 형식 또는 크기 초과",
            content = @Content(
                schema = @Schema(implementation = ErrorResponse.class)
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
    public ResponseEntity<BaseResponse<UserResponseDto>> uploadProfileImage(
        @RequestPart("profileImage") MultipartFile profileImage,
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        User user = userService.uploadProfileImage(profileImage, userPrincipal.getUser());
        UserInfo userInfo = userService.getUserInfoByUser(user);
        return SuccessResponse.of(ApiStatus._OK, UserResponseDto.from(userInfo));
    }

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
                schema = @Schema(implementation = UserDtoSuccessResponse.class)
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
    public ResponseEntity<BaseResponse<UserResponseDto>> getMe(
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        User user = userService.getUserById(userPrincipal.getUser().getId());
        UserInfo userInfo = userService.getUserInfoByUser(user);
        return SuccessResponse.of(ApiStatus._OK, UserResponseDto.from(userInfo));
    }

    @GetMapping("/me/projects")
    @Operation(
        summary = "현재 사용자의 프로젝트 목록 조회",
        description = "현재 로그인된 사용자의 프로젝트 목록을 조회합니다.",
        security = @SecurityRequirement(name = "JWT")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "프로젝트 목록 조회 성공",
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
        )
    })
    public ResponseEntity<BaseResponse<List<ProjectResponseDto>>> getMyProjects(
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return SuccessResponse.of(
            ApiStatus._OK,
            projectService.getUserProjects(userPrincipal.getUser().getId()).stream()
                .map(project -> projectService.getProjectInfoByProject(
                    project,
                    userPrincipal.getUser())
                )
                .map(ProjectResponseDto::from)
                .toList()
        );
    }

    @GetMapping("/me/followees")
    public ResponseEntity<BaseResponse<List<SimpleUserInfo>>> getMyFollowees(
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return SuccessResponse.of(
            ApiStatus._OK,
            followService.getUserFollowees(userPrincipal.getUser())
        );
    }

    @GetMapping("/me/followers")
    public ResponseEntity<BaseResponse<List<SimpleUserInfo>>> getMyFollowers(
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return SuccessResponse.of(
            ApiStatus._OK,
            followService.getUserFollowers(userPrincipal.getUser())
        );
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
                schema = @Schema(implementation = UserDtoSuccessResponse.class)
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
    public ResponseEntity<BaseResponse<UserResponseDto>> getUser(@PathVariable UUID userId) {
        User user = userService.getUserById(userId);
        UserInfo userInfo = userService.getUserInfoByUser(user);
        return SuccessResponse.of(ApiStatus._OK, UserResponseDto.from(userInfo));
    }

    @GetMapping
    @Operation(
        summary = "사용자 검색",
        description = "이름 또는 이메일로 사용자를 검색합니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "검색 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    public ResponseEntity<BaseResponse<List<SimpleUserInfo>>> searchUsers(
        @RequestParam String query
    ) {
        List<User> users = userService.searchUsers(query);
        return SuccessResponse.of(ApiStatus._OK, users.stream().map(SimpleUserInfo::from).toList());
    }

    @PutMapping("/me")
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
                schema = @Schema(implementation = UserDtoSuccessResponse.class)
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
        @Valid @RequestBody UserInputDto userInputDto,
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        User user = userService.updateUser(userInputDto, userPrincipal.getUser());
        UserInfo userInfo = userService.getUserInfoByUser(user);
        return SuccessResponse.of(ApiStatus._OK, UserResponseDto.from(userInfo));
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
    public ResponseEntity<BaseResponse<Object>> deleteMe(
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        userService.deleteUser(userPrincipal.getUser());
        return SuccessResponse.of(ApiStatus._NO_CONTENT, null);
    }
}