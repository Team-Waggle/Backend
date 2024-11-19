package com.waggle.domain.auth.controller;

import com.waggle.domain.auth.dto.AccessTokenResponse;
import com.waggle.domain.auth.service.AuthService;
import com.waggle.domain.user.entity.User;
import com.waggle.global.exception.JwtTokenException;
import com.waggle.global.response.ApiResponseEntity;
import com.waggle.global.response.code.ErrorResponseCode;
import com.waggle.global.response.code.SuccessResponseCode;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/token/reissue")
    public ResponseEntity<ApiResponseEntity<Object>> reissueAccessToken(@CookieValue(name = "refresh_token", required = false) String refreshToken) {
        if (refreshToken == null) {
            return ApiResponseEntity.onFailure(ErrorResponseCode._UNAUTHORIZED);
        }

        AccessTokenResponse accessToken = authService.reissueAccessToken(refreshToken);
        return ApiResponseEntity.onSuccess(SuccessResponseCode._CREATED_ACCESS_TOKEN, accessToken);
    }

    @PostMapping("/token/exchange")
    public ResponseEntity<?> exchangeToken(@RequestParam String temporaryToken) {
        try {
            String accessToken = authService.exchangeTemporaryToken(temporaryToken);
            return ApiResponseEntity.onSuccess(SuccessResponseCode._CREATED_ACCESS_TOKEN, accessToken);
        } catch (JwtTokenException e) {
            return ApiResponseEntity.onFailure(ErrorResponseCode._UNAUTHORIZED);
        }
    }

    @GetMapping("/current-user")
    public ResponseEntity<?> fetchCurrentUser() {
        User currentUserUser = authService.getCurrentUser();
        if (currentUserUser == null) {
            return ApiResponseEntity.onFailure(ErrorResponseCode._UNAUTHORIZED);
        }
        return ApiResponseEntity.onSuccess(SuccessResponseCode._OK, currentUserUser);
    }
}
