package com.waggle.domain.auth.controller;

import com.waggle.domain.auth.dto.AccessTokenResponse;
import com.waggle.domain.auth.service.AuthService;
import com.waggle.domain.user.entity.User;
import com.waggle.global.response.ApiResponseEntity;
import com.waggle.global.response.code.ErrorResponseCode;
import com.waggle.global.response.code.SuccessResponseCode;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/reissue/access-token")
    public ResponseEntity<ApiResponseEntity<Object>> reissueAccessToken(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        String refreshToken = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh_token")) {
                refreshToken = cookie.getValue();
                break;
            }
        }

        if (refreshToken == null) {
            return ApiResponseEntity.onFailure(ErrorResponseCode._UNAUTHORIZED);
        }

        AccessTokenResponse accessToken = authService.reissueAccessToken(refreshToken);
        return ApiResponseEntity.onSuccess(SuccessResponseCode._CREATED_ACCESS_TOKEN, accessToken);
    }

    @GetMapping("/current-user")
    public User fetchCurrentUser() {
        return authService.getCurrentUser();
    }
}
