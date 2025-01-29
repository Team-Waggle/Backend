package com.waggle.global.secure.oauth2.handler;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.waggle.domain.auth.entity.RefreshToken;
import com.waggle.domain.auth.repository.RefreshTokenRepository;
import com.waggle.domain.user.entity.User;
import com.waggle.domain.user.repository.UserRepository;
import com.waggle.global.secure.jwt.JwtUtil;
import com.waggle.global.secure.oauth2.adapter.GoogleUserInfoAdapter;
import com.waggle.global.secure.oauth2.adapter.KakaoUserInfoAdapter;
import com.waggle.global.secure.oauth2.adapter.NaverUserInfoAdapter;
import com.waggle.global.secure.oauth2.adapter.OAuth2UserInfo;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${JWT_REDIRECT_URI}")
    private String REDIRECT_URI; // 프론트엔드로 Jwt 토큰을 리다이렉트할 URI
    @Value("${JWT_ACCESS_TOKEN_EXPIRE_TIME}")
    private long ACCESS_TOKEN_EXPIRATION_TIME; // 액세스 토큰 유효기간
    @Value("${JWT_REFRESH_TOKEN_EXPIRE_TIME}")
    private long REFRESH_TOKEN_EXPIRATION_TIME; // 리프레쉬 토큰 유효기간

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private OAuth2UserInfo oAuth2UserInfo = null;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication; // 토큰
        final String provider = token.getAuthorizedClientRegistrationId(); // provider 추출

        // 구글 || 카카오 || 네이버 로그인 요청
        switch (provider) {
            case "google" -> {
                log.info("구글 로그인 요청");
                oAuth2UserInfo = new GoogleUserInfoAdapter(token.getPrincipal().getAttributes());
            }
            case "kakao" -> {
                log.info("카카오 로그인 요청");
                oAuth2UserInfo = new KakaoUserInfoAdapter(token.getPrincipal().getAttributes());
            }
            case "naver" -> {
                log.info("네이버 로그인 요청");
                oAuth2UserInfo = new NaverUserInfoAdapter((Map<String, Object>) token.getPrincipal().getAttributes().get("response"));
            }
        }

        // 정보 추출
        String providerId = oAuth2UserInfo.getProviderId();
        String name = oAuth2UserInfo.getName();
        String email = oAuth2UserInfo.getEmail();
        String profileImage = oAuth2UserInfo.getProfileImage();

        User existUser = userRepository.findByProviderId(providerId);
        User user;

        if (existUser == null) {
            // 신규 유저인 경우
            log.info("신규 유저입니다. 등록을 진행합니다.");

            user = User.builder()
                    .name(name)
                    .email(email)
                    .profileImageUrl(profileImage)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(user);
        } else {
            // 기존 유저인 경우
            log.info("기존 유저입니다.");
            refreshTokenRepository.deleteByUserId(existUser.getId());
            user = existUser;
        }

        log.info("유저 이름 : {}", name);
        log.info("유저 이메일 : {}", email);
        log.info("PROVIDER : {}", provider);
        log.info("PROVIDER_ID : {}", providerId);

        // 리프레쉬 토큰 발급 후 저장
        String refreshToken = jwtUtil.generateRefreshToken(user.getId(), REFRESH_TOKEN_EXPIRATION_TIME);

        RefreshToken newRefreshToken = RefreshToken.builder()
                .user(user)
                .token(refreshToken)
                .build();
        refreshTokenRepository.save(newRefreshToken);

        // 액세스 토큰 발급
        String accessToken = jwtUtil.generateAccessToken(user.getId(), ACCESS_TOKEN_EXPIRATION_TIME);

        // 임시 토큰 생성 (예: UUID + userId를 조합)
        String temporaryToken = UUID.randomUUID().toString();
        
        // Redis에 임시 토큰과 액세스 토큰을 저장 (5분 만료)
        redisTemplate.opsForValue().set(
            "TEMP_TOKEN:" + temporaryToken,
            accessToken,
            Duration.ofMinutes(5)
        );

        Cookie cookie = new Cookie("refresh_token", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(30 * 24 * 60 * 60); // 30일
        response.addCookie(cookie);

        // 액세스 토큰, 리프레쉬 토큰을 담아 리다이렉트
        String redirectUri = String.format(REDIRECT_URI, temporaryToken);
        getRedirectStrategy().sendRedirect(request, response, redirectUri);
    }
}
