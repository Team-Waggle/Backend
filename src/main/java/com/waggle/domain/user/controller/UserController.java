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
}