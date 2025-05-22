package com.waggle.global.secure.oauth2.handler;

import com.waggle.domain.user.entity.User;
import com.waggle.domain.user.repository.UserRepository;
import com.waggle.global.secure.jwt.JwtUtil;
import com.waggle.global.secure.oauth2.adapter.GoogleUserInfoAdapter;
import com.waggle.global.secure.oauth2.adapter.KakaoUserInfoAdapter;
import com.waggle.global.secure.oauth2.adapter.NaverUserInfoAdapter;
import com.waggle.global.secure.oauth2.adapter.OAuth2UserInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${SPRING_PROFILES_ACTIVE}")
    private String profile; // 현재 프로파일
    @Value("${LOCAL_FULL_URL}")
    private String localUrl; // 로컬 서버 URL
    @Value("${PROD_HTTPS_FULL_URL}")
    private String prodUrl; // 프로덕션 서버 URL
    @Value("${LOCAL_LOGIN_PROCESS_ENDPOINT}")
    private String localLoginProcessEndpoint; // 로컬 로그인 처리 엔드포인트
    @Value("${PROD_LOGIN_PROCESS_ENDPOINT}")
    private String prodLoginProcessEndpoint; // 프로덕션 로그인 처리 엔드포인트
    private OAuth2UserInfo oAuth2UserInfo = null;

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException {
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
                oAuth2UserInfo = new NaverUserInfoAdapter(
                    (Map<String, Object>) token.getPrincipal().getAttributes().get("response"));
            }
        }

        // 정보 추출
        String providerId = oAuth2UserInfo.getProviderId();
        String name = oAuth2UserInfo.getName();
        String email = oAuth2UserInfo.getEmail();
        String profileImage = oAuth2UserInfo.getProfileImage();

        User user = userRepository.findByProviderId(providerId)
            .orElseGet(() -> {
                log.info("신규 유저입니다. 등록을 진행합니다.");
                return userRepository.save(User.builder()
                    .name(name)
                    .email(email)
                    .profileImageUrl(profileImage)
                    .provider(provider)
                    .providerId(providerId)
                    .build());
            });

        if (user.getId() != null) {
            log.info("기존 유저입니다.");
            redisTemplate.delete("REFRESH_TOKEN:" + user.getId());
        }

        log.info("유저 이름 : {}", name);
        log.info("유저 이메일 : {}", email);
        log.info("PROVIDER : {}", provider);
        log.info("PROVIDER_ID : {}", providerId);

        String accessToken = jwtUtil.generateAccessToken(user.getId());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());
        redisTemplate.opsForValue().set(
            "REFRESH_TOKEN:" + user.getId(),
            refreshToken,
            Duration.ofMillis(jwtUtil.refreshTokenExpirationTime)
        );

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", refreshToken)
            .httpOnly(true)
            .secure(true)
            .sameSite("None")
            .path("/")
            .maxAge(jwtUtil.refreshTokenExpirationTime)
            .build();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        // 액세스 토큰, 리프레쉬 토큰을 담아 리다이렉트
        String redirectUri = "";
        if (profile.equals("prod")) {
            redirectUri = "http://localhost:5173" + prodLoginProcessEndpoint;
        }
        if (profile.equals("local")) {
            redirectUri = localUrl + localLoginProcessEndpoint;
        }
        redirectUri = redirectUri +
            "?is_exist_user=" + (user.getId() != null) +
            "&access_token=" + accessToken;
        getRedirectStrategy().sendRedirect(request, response, redirectUri);
    }
}
