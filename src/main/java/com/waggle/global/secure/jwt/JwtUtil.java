package com.waggle.global.secure.jwt;

import com.waggle.global.exception.JwtTokenException;
import com.waggle.global.response.ApiStatus;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtUtil {

    @Value("${JWT_ACCESS_TOKEN_EXPIRE_TIME}")
    public long accessTokenExpirationTime; // 액세스 토큰 유효기간
    @Value("${JWT_REFRESH_TOKEN_EXPIRE_TIME}")
    public long refreshTokenExpirationTime; // 리프레쉬 토큰 유효기간
    @Value("${JWT_SECRET_KEY}")
    private String secretKey;

    // 액세스 토큰을 발급하는 메서드
    public String generateAccessToken(UUID userId) {
        log.info("액세스 토큰이 발행되었습니다.");

        return generateToken(userId, accessTokenExpirationTime);
    }

    // 리프레쉬 토큰을 발급하는 메서드
    public String generateRefreshToken(UUID userId) {
        log.info("리프레쉬 토큰이 발행되었습니다.");

        return generateToken(userId, refreshTokenExpirationTime);
    }

    // 응답 헤더에서 액세스 토큰을 반환하는 메서드
    public String getTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new JwtTokenException(ApiStatus._INVALID_ACCESS_TOKEN);
        }
        return authorizationHeader.substring(7);
    }

    // 토큰에서 유저 id를 반환하는 메서드
    public String getUserIdFromToken(String token) {
        try {
            String userId = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("userId", String.class);
            log.info("유저 id를 반환합니다.");
            return userId;
        } catch (JwtException | IllegalArgumentException e) {
            // 토큰이 유효하지 않은 경우
            log.warn("유효하지 않은 토큰입니다.");
            throw new JwtTokenException(ApiStatus._INVALID_TOKEN);
        }
    }

    public boolean validateToken(String token) {
        if (token == null || token.isBlank()) {
            log.warn("JWT 토큰이 null이거나 비어 있습니다");
            return false;
        }

        try {
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);

            String userId = getUserIdFromToken(token);
            if (userId == null || userId.isBlank()) {
                log.warn("토큰에 userId 클레임이 없거나 비어 있습니다");
                return false;
            }

            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.warn("유효하지 않은 JWT 서명: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.warn("만료된 JWT 토큰: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("지원되지 않는 JWT 토큰: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("JWT claims 문자열이 비어있음: {}", e.getMessage());
        } catch (Exception e) {
            log.error("JWT 토큰 검증 중 예상치 못한 오류: {}", e.getMessage());
        }
        return false;
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String generateToken(UUID userId, long expirationMillis) {
        return Jwts.builder()
            .claim("userId", userId.toString())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + expirationMillis))
            .signWith(this.getSigningKey())
            .compact();
    }
}
