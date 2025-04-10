package com.waggle.domain.auth.service;

import com.waggle.domain.auth.dto.AccessTokenVo;
import com.waggle.domain.user.entity.User;
import com.waggle.domain.user.repository.UserRepository;
import com.waggle.global.exception.JwtTokenException;
import com.waggle.global.response.ApiStatus;
import com.waggle.global.secure.jwt.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final String REFRESH_TOKEN_PREFIX = "REFRESH_TOKEN:";
    private static final String TEMP_TOKEN_PREFIX = "TEMP_TOKEN:";

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public User getCurrentUser() {
        try {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (requestAttributes == null) {
                throw new JwtTokenException(ApiStatus._INVALID_ACCESS_TOKEN);
            }

            String authorizationHeader = requestAttributes.getRequest().getHeader("Authorization");
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                log.warn("Authorization header not found");
                throw new JwtTokenException(ApiStatus._INVALID_ACCESS_TOKEN);
            }

            String token = jwtUtil.getTokenFromHeader(authorizationHeader);
            if (!jwtUtil.validateToken(token)) {
                log.warn("토큰 검증 실패");
                throw new JwtTokenException(ApiStatus._INVALID_ACCESS_TOKEN);
            }

            String userId = jwtUtil.getUserIdFromToken(token);
            return userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> {
                    log.error("User not found with id: {}", userId);
                    return new JwtTokenException(ApiStatus._INVALID_ACCESS_TOKEN);
                });
        } catch (ExpiredJwtException e) {
            log.warn("만료된 토큰: {}", e.getMessage());
            throw new JwtTokenException(ApiStatus._EXPIRED_TOKEN);
        } catch (JwtTokenException e) {
            throw new JwtTokenException(ApiStatus._INVALID_TOKEN);
        } catch (Exception e) {
            log.error("Error while getting current user", e);
            throw new JwtTokenException(ApiStatus._INVALID_TOKEN);
        }
    }

    @Override
    public AccessTokenVo reissueAccessToken(String refreshToken) {
        try {
            // 토큰 기본 검증
            if (!jwtUtil.validateToken(refreshToken)) {
                log.warn("유효하지 않은 리프레시 토큰");
                throw new JwtTokenException(ApiStatus._INVALID_REFRESH_TOKEN);
            }

            // 토큰에서 사용자 ID 추출
            String userId = jwtUtil.getUserIdFromToken(refreshToken);

            // Redis에서 저장된 리프레시 토큰 조회
            String redisKey = REFRESH_TOKEN_PREFIX + userId;
            String storedRefreshToken = redisTemplate.opsForValue().get(redisKey);

            // 저장된 토큰 검증
            if (storedRefreshToken == null) {
                log.warn("Redis에 저장된 리프레시 토큰이 없음: userId={}", userId);
                throw new JwtTokenException(ApiStatus._INVALID_REFRESH_TOKEN);
            }

            if (!storedRefreshToken.equals(refreshToken)) {
                log.warn("요청된 리프레시 토큰과 저장된 토큰이 불일치: userId={}", userId);
                throw new JwtTokenException(ApiStatus._INVALID_REFRESH_TOKEN);
            }

            // 액세스 토큰 재발급
            log.info("액세스 토큰 재발급: userId={}", userId);
            String accessToken = jwtUtil.generateAccessToken(UUID.fromString(userId));
            return AccessTokenVo.from(accessToken);

        } catch (ExpiredJwtException e) {
            // 만료된 토큰인 경우 처리
            log.warn("리프레시 토큰 만료: {}", e.getMessage());

            try {
                // 만료된 토큰에서도 userId는 추출 가능
                String userId = e.getClaims().get("userId", String.class);
                if (userId != null) {
                    redisTemplate.delete(REFRESH_TOKEN_PREFIX + userId);  // 만료된 토큰 삭제
                }
            } catch (Exception ex) {
                log.error("만료된 토큰에서 userId 추출 실패", ex);
            }

            throw new JwtTokenException(ApiStatus._EXPIRED_TOKEN);
        } catch (JwtTokenException e) {
            throw e;
        } catch (Exception e) {
            log.error("토큰 재발급 중 오류 발생", e);
            throw new JwtTokenException(ApiStatus._INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public AccessTokenVo exchangeTemporaryToken(String temporaryToken) {
        String key = TEMP_TOKEN_PREFIX + temporaryToken;
        String accessToken = redisTemplate.opsForValue().get(key);

        if (accessToken == null) {
            throw new JwtTokenException(ApiStatus._INVALID_TEMPORARY_TOKEN);
        }

        // 임시 토큰 삭제
        redisTemplate.delete(key);

        return AccessTokenVo.from(accessToken);
    }

    @Override
    public void deleteRefreshToken(String refreshToken) {
        if (refreshToken == null) {
            throw new JwtTokenException(ApiStatus._REFRESH_TOKEN_NOT_FOUND);
        }

        String userId = jwtUtil.getUserIdFromToken(refreshToken);
        redisTemplate.delete(REFRESH_TOKEN_PREFIX + userId);
    }
}
