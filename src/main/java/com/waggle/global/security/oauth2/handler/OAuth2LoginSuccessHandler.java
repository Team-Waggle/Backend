package com.waggle.global.security.oauth2.handler;

import com.waggle.domain.user.entity.User;
import com.waggle.global.security.jwt.JwtUtil;
import com.waggle.global.security.oauth2.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userPrincipal.getUser();

        log.info("OAuth2 login successful for user: {}", user.getEmail());

        redisTemplate.delete("REFRESH_TOKEN:" + user.getId());

        String accessToken = jwtUtil.generateAccessToken(user.getId());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());

        redisTemplate.opsForValue().set(
            "REFRESH_TOKEN:" + user.getId(),
            refreshToken,
            Duration.ofMillis(jwtUtil.getRefreshTokenExpirationTime())
        );

        jwtUtil.addAccessTokenCookie(response, accessToken);
        jwtUtil.addRefreshTokenCookie(response, refreshToken);

//        String redirectUri = frontendUrl + "/auth/callback?success=true";
        String origin = request.getHeader("Origin");
        String redirectBase;

        if ("http://localhost:5173".equals(origin)) {
            redirectBase = "http://localhost:5173";
        } else {
            redirectBase = frontendUrl;
        }

        String redirectUri =
            redirectBase + "/auth/callback?success=true&access_token=" + accessToken
                + "&refresh_token=" + refreshToken;
        response.sendRedirect(redirectUri);
    }
}
