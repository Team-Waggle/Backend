package com.waggle.domain.auth.service;

import com.waggle.domain.auth.dto.AccessTokenVo;
import com.waggle.global.exception.JwtTokenException;
import com.waggle.global.response.ApiStatus;
import com.waggle.global.security.jwt.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final String REFRESH_TOKEN_PREFIX = "REFRESH_TOKEN:";
    private static final String TEMP_TOKEN_PREFIX = "TEMP_TOKEN:";

    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public AccessTokenVo reissueAccessToken(String refreshToken) {
        try {
            // 토큰 기본 검증
            if (!jwtUtil.validateToken(refreshToken)) {
                log.warn("Invalid refresh token provided");
                throw new JwtTokenException(ApiStatus._INVALID_REFRESH_TOKEN);
            }

            // 토큰에서 사용자 ID 추출
            String userId = jwtUtil.getUserIdFromToken(refreshToken);

            // Redis에서 저장된 리프레시 토큰 조회
            String redisKey = REFRESH_TOKEN_PREFIX + userId;
            String storedRefreshToken = redisTemplate.opsForValue().get(redisKey);

            // 저장된 토큰 검증
            if (storedRefreshToken == null) {
                log.warn("No refresh token found in Redis for userId: {}", userId);
                throw new JwtTokenException(ApiStatus._INVALID_REFRESH_TOKEN);
            }

            if (!storedRefreshToken.equals(refreshToken)) {
                log.warn("Refresh token mismatch for userId: {}", userId);
                throw new JwtTokenException(ApiStatus._INVALID_REFRESH_TOKEN);
            }

            // 액세스 토큰 재발급
            log.info("Access token reissued for userId: {}", userId);
            String accessToken = jwtUtil.generateAccessToken(UUID.fromString(userId));
            return AccessTokenVo.from(accessToken);
        } catch (ExpiredJwtException e) {
            // 만료된 토큰인 경우 처리
            log.warn("Refresh token expired: {}", e.getMessage());

            try {
                // 만료된 토큰에서도 userId는 추출 가능
                String userId = e.getClaims().get("userId", String.class);
                if (userId != null) {
                    redisTemplate.delete(REFRESH_TOKEN_PREFIX + userId);
                    log.info("Deleted expired refresh token for userId: {}", userId);
                }
            } catch (Exception ex) {
                log.error("Failed to extract userId from expired token", ex);
            }

            throw new JwtTokenException(ApiStatus._EXPIRED_TOKEN);
        } catch (JwtTokenException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during token reissue", e);
            throw new JwtTokenException(ApiStatus._INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void deleteRefreshToken(String refreshToken) {
        if (refreshToken == null) {
            log.warn("Attempted to delete null refresh token");
            throw new JwtTokenException(ApiStatus._REFRESH_TOKEN_NOT_FOUND);
        }

        try {
            String userId = jwtUtil.getUserIdFromToken(refreshToken);
            String redisKey = REFRESH_TOKEN_PREFIX + userId;

            Boolean isDeleted = redisTemplate.delete(redisKey);
            if (Boolean.TRUE.equals(isDeleted)) {
                log.info("Refresh token deleted for userId: {}", userId);
            } else {
                log.warn("No refresh token found to delete for userId: {}", userId);
            }
        } catch (Exception e) {
            log.error("Failed to delete refresh token", e);
            throw new JwtTokenException(ApiStatus._INVALID_REFRESH_TOKEN);
        }
    }
}
