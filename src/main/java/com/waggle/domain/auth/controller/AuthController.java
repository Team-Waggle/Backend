package com.waggle.domain.auth.controller;

import com.waggle.domain.auth.dto.AccessTokenVo;
import com.waggle.domain.auth.service.AuthService;
import com.waggle.global.exception.JwtTokenException;
import com.waggle.global.response.ApiStatus;
import com.waggle.global.response.BaseResponse;
import com.waggle.global.response.ErrorResponse;
import com.waggle.global.response.SuccessResponse;
import com.waggle.global.response.swagger.AccessTokenSuccessResponse;
import com.waggle.global.security.jwt.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증", description = "인증 관련 API")
@RestController
@RequestMapping("/api/v2/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/tokens/reissue")
    @Operation(
        summary = "액세스 토큰 재발급",
        description = """
            액세스 토큰을 재발급합니다.
            
            ⚠️ 실제 API 호출 시에는 쿠키의 refresh_token이 자동으로 사용됩니다.
            Swagger UI 테스트 시에만 쿠키값을 확인하여 수동으로 refresh_token 값을 입력해주세요.
            """,
        security = @SecurityRequirement(name = "OAuth2")
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "토큰 재발급 성공", content = @Content(schema = @Schema(implementation = AccessTokenSuccessResponse.class))),
        @ApiResponse(responseCode = "401", description = "유효하지 않은 리프레시 토큰", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<BaseResponse<Object>> reissueAccessToken(
        HttpServletResponse response,
        @CookieValue(name = "refresh_token", required = false) String refreshToken
    ) {
        if (refreshToken == null) {
            throw new JwtTokenException(ApiStatus._REFRESH_TOKEN_NOT_FOUND);
        }
        jwtUtil.deleteTokenCookies(response);
        try {
            AccessTokenVo accessToken = authService.reissueAccessToken(refreshToken);
            jwtUtil.addAccessTokenCookie(response, accessToken.accessToken());
            return SuccessResponse.of(ApiStatus._REISSUE_ACCESS_TOKEN, accessToken);
        } catch (JwtTokenException e) {
            log.warn("Token reissue failed: {}", e.getMessage());
            throw e;
        }
    }

    @PostMapping("/logout")
    @Operation(
        summary = "로그아웃",
        description = """
            서버에서 해당 Refresh Token이 어떤 사용자의 것인지에 대한 기록을 삭제하여 최종적으로 로그아웃합니다.
            
            ⚠️ 로그아웃 후에는 꼭 로컬 스토리지에 저장된 토큰을 삭제하고 새로고침을 해주세요.
            """,
        security = @SecurityRequirement(name = "OAuth2")
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "로그아웃 성공", content = @Content()),
        @ApiResponse(responseCode = "401", description = "유효하지 않은 리프레시 토큰", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<BaseResponse<Object>> logout(
        @CookieValue(name = "refresh_token", required = false) String refreshToken) {
        authService.deleteRefreshToken(refreshToken);

        return SuccessResponse.of(ApiStatus._NO_CONTENT, null);
    }
}
