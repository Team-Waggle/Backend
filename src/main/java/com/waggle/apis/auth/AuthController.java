package com.waggle.apis.auth;

import com.waggle.apis.auth.dto.AccessTokenResponse;
import com.waggle.apis.user.entity.User;
import com.waggle.global.ApiResponse;
import com.waggle.global.status.ErrorStatus;
import com.waggle.global.status.SuccessStatus;
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
    public ResponseEntity<ApiResponse<Object>> reissueAccessToken(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        String refreshToken = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh_token")) {
                refreshToken = cookie.getValue();
                break;
            }
        }

        if (refreshToken == null) {
            return ApiResponse.onFailure(ErrorStatus._UNAUTHORIZED);
        }

        AccessTokenResponse accessToken = authService.reissueAccessToken(refreshToken);
        return ApiResponse.onSuccess(SuccessStatus._CREATED_ACCESS_TOKEN, accessToken);
    }

    @GetMapping("/current-user")
    public User fetchCurrentUser() {
        return authService.getCurrentUser();
    }
}
