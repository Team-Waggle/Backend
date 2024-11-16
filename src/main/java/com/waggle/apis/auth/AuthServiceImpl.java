package com.waggle.apis.auth;

import com.waggle.apis.auth.dto.*;
import com.waggle.apis.auth.entity.RefreshToken;
import com.waggle.apis.auth.entity.RefreshTokenRepository;
import com.waggle.apis.user.entity.UserRepository;
import com.waggle.apis.user.entity.User;
import com.waggle.auth.JwtUtil;
import com.waggle.exception.JwtTokenException;
import com.waggle.exception.status.JwtTokenErrorStatus;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);
    @Value("${JWT_ACCESS_TOKEN_EXPIRE_TIME}")
    private long ACCESS_TOKEN_EXPIRATION_TIME; // 액세스 토큰 유효기간

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    @Override
    public AccessTokenResponse reissueAccessToken(String refreshToken) {
        String userId = jwtUtil.getUserIdFromToken(refreshToken);
        RefreshToken existRefreshToken = refreshTokenRepository.findByUserId(UUID.fromString(userId));
        String accessToken = null;

        if (!existRefreshToken.getToken().equals(refreshToken) || jwtUtil.isTokenExpired(refreshToken)) {
            // 리프레쉬 토큰이 다르거나, 만료된 경우
            throw new JwtTokenException(JwtTokenErrorStatus.INVALID_REFRESH_TOKEN); // 401 에러를 던져 재로그인을 요청
        } else {
            // 액세스 토큰 재발급
            accessToken = jwtUtil.generateAccessToken(UUID.fromString(userId), ACCESS_TOKEN_EXPIRATION_TIME);
        }

        return AccessTokenResponse.builder()
                .accessToken(accessToken)
                .build();
    }

    @Override
    public User getCurrentUser() {
        OAuth2UserInfo oAuth2UserInfo = null;

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String provider = token.getAuthorizedClientRegistrationId();

        switch (provider) {
            case "google":
                oAuth2UserInfo = new GoogleUserInfo(token.getPrincipal().getAttributes());
                break;
            case "kakao":
                oAuth2UserInfo = new KakaoUserInfo(token.getPrincipal().getAttributes());
                break;
            case "naver":
                oAuth2UserInfo = new NaverUserInfo((Map<String, Object>) token.getPrincipal().getAttributes().get("response"));
                break;
        }

        User user = userRepository.findByProviderId(oAuth2UserInfo.getProviderId());

        if (user == null) {
            return null;
        }

        return user;
    }
}
