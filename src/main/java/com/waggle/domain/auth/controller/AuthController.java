package com.waggle.domain.auth.controller;

import com.waggle.domain.auth.dto.AccessTokenResponse;
import com.waggle.domain.auth.service.AuthService;
import com.waggle.domain.user.entity.User;
import com.waggle.global.exception.JwtTokenException;
import com.waggle.global.response.ApiResponseEntity;
import com.waggle.global.response.code.ErrorResponseCode;
import com.waggle.global.response.code.SuccessResponseCode;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증", description = "인증 관련 API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/token/reissue")
    @Operation(summary = "액세스 토큰 재발급", description = "액세스 토큰을 재발급합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "토큰 재발급 성공"),
        @ApiResponse(responseCode = "401", description = "유효하지 않은 리프레시 토큰")
    })
    public ResponseEntity<ApiResponseEntity<Object>> reissueAccessToken(@CookieValue(name = "refresh_token", required = false) String refreshToken) {
        if (refreshToken == null) {
            return ApiResponseEntity.onFailure(ErrorResponseCode._UNAUTHORIZED);
        }

        AccessTokenResponse accessToken = authService.reissueAccessToken(refreshToken);
        return ApiResponseEntity.onSuccess(SuccessResponseCode._CREATED_ACCESS_TOKEN, accessToken);
    }

    @PostMapping("/token/exchange")
    @Operation(summary = "임시 토큰 교환", description = "임시 토큰을 교환하여 액세스 토큰을 발급합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "토큰 교환 성공"),
        @ApiResponse(responseCode = "401", description = "유효하지 않은 임시 토큰")
    })
    public ResponseEntity<?> exchangeToken(@RequestParam String temporaryToken) {
        try {
            String accessToken = authService.exchangeTemporaryToken(temporaryToken);
            return ApiResponseEntity.onSuccess(SuccessResponseCode._CREATED_ACCESS_TOKEN, accessToken);
        } catch (JwtTokenException e) {
            return ApiResponseEntity.onFailure(ErrorResponseCode._UNAUTHORIZED);
        }
    }

    @GetMapping("/current-user")
    @Operation(summary = "현재 사용자 조회", description = "현재 사용자를 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "사용자 조회 성공"),
        @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    public ResponseEntity<?> fetchCurrentUser() {
        User currentUserUser = authService.getCurrentUser();
        if (currentUserUser == null) {
            return ApiResponseEntity.onFailure(ErrorResponseCode._UNAUTHORIZED);
        }
        return ApiResponseEntity.onSuccess(SuccessResponseCode._OK, currentUserUser);
    }
}
