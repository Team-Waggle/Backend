package com.waggle.global.security.jwt;

import com.waggle.global.exception.JwtTokenException;
import com.waggle.global.response.ApiStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtUtil {

    private static final String TOKEN_TYPE_ACCESS = "ACCESS";
    private static final String TOKEN_TYPE_REFRESH = "REFRESH";
    private static final String ACCESS_TOKEN_COOKIE_NAME = "access_token";
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";

    @Value("${JWT_SECRET_KEY}")
    private String secretKey;

    @Value("${JWT_ACCESS_TOKEN_EXPIRE_TIME}")
    private long accessTokenExpirationTime;

    @Getter
    @Value("${JWT_REFRESH_TOKEN_EXPIRE_TIME}")
    private long refreshTokenExpirationTime;

    public String generateAccessToken(UUID userId) {
        log.info("Generated access token for userId: {}", userId);
        return generateToken(userId, accessTokenExpirationTime, TOKEN_TYPE_ACCESS);
    }

    public String generateRefreshToken(UUID userId) {
        log.info("Generated refresh token for userId: {}", userId);
        return generateToken(userId, refreshTokenExpirationTime, TOKEN_TYPE_REFRESH);
    }

    public String getAccessTokenFromCookie(HttpServletRequest request) {
        return getTokenFromCookie(request, ACCESS_TOKEN_COOKIE_NAME);
    }

    public String extractToken(HttpServletRequest request) {
        // 쿠키에서 토큰 추출 시도
        String tokenFromCookie = getAccessTokenFromCookie(request);
        if (tokenFromCookie != null) {
            log.debug("Token extracted from cookie");
            return tokenFromCookie;
        }

        // Authorization 헤더에서 토큰 추출 시도 (테스트)
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            try {
                String tokenFromHeader = getTokenFromHeader(authHeader);
                log.debug("Token extracted from Authorization header");
                return tokenFromHeader;
            } catch (Exception e) {
                log.debug("Invalid Authorization header format");
            }
        }

        log.debug("No token found in cookies or Authorization header");
        return null;
    }

    // Authorization 헤더에서 토큰 추출
    public String getTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new JwtTokenException(ApiStatus._INVALID_ACCESS_TOKEN);
        }
        return authorizationHeader.substring(7);
    }

    // 응답에 액세스 토큰 쿠키 추가
    public void addAccessTokenCookie(HttpServletResponse response, String token) {
        addTokenCookie(response, ACCESS_TOKEN_COOKIE_NAME, token, accessTokenExpirationTime);
    }

    // 응답에 리프레시 토큰 쿠키 추가
    public void addRefreshTokenCookie(HttpServletResponse response, String token) {
        addTokenCookie(response, REFRESH_TOKEN_COOKIE_NAME, token, refreshTokenExpirationTime);
    }

    // 토큰 쿠키 삭제 (로그아웃용)
    public void deleteTokenCookies(HttpServletResponse response) {
        deleteTokenCookie(response, ACCESS_TOKEN_COOKIE_NAME);
        deleteTokenCookie(response, REFRESH_TOKEN_COOKIE_NAME);
        log.info("Deleted all token cookies");
    }

    public String getUserIdFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

            String userId = claims.get("userId", String.class);
            if (userId == null || userId.isBlank()) {
                log.warn("Token does not contain userId claim or userId is empty");
                throw new JwtTokenException(ApiStatus._INVALID_TOKEN);
            }

            log.debug("Extracted userId from token: {}", userId);
            return userId;

        } catch (JwtException | IllegalArgumentException e) {
            log.error("Failed to extract userId from token: {}", e.getMessage());
            throw new JwtTokenException(ApiStatus._INVALID_TOKEN);
        }
    }

    public boolean validateToken(String token) {
        if (token == null || token.isBlank()) {
            log.warn("JWT token is null or empty");
            return false;
        }

        try {
            Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token);

            log.debug("Token validation successful");
            return true;

        } catch (ExpiredJwtException e) {
            log.warn("JWT token has expired: {}", e.getMessage());
            return false;
        } catch (SecurityException | MalformedJwtException e) {
            log.warn("Invalid JWT signature: {}", e.getMessage());
            return false;
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT token: {}", e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            log.warn("JWT claims string is empty: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Unexpected error during JWT token validation: {}", e.getMessage());
            return false;
        }
    }

    private String getTokenFromCookie(HttpServletRequest request, String cookieName) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookieName.equals(cookie.getName())) {
                    String token = cookie.getValue();
                    log.debug("Found token in cookie: {}", cookieName);
                    return token;
                }
            }
        }
        log.debug("Token not found in cookie: {}", cookieName);
        return null;
    }

    private void addTokenCookie(
        HttpServletResponse response,
        String cookieName,
        String token,
        long expirationMillis
    ) {
        Cookie cookie = new Cookie(cookieName, token);
        cookie.setHttpOnly(true); // XSS 방지
        cookie.setSecure(true); // HTTPS에서만 전송 (개발시에는 false)
        cookie.setPath("/"); // 모든 경로에서 사용
        cookie.setMaxAge((int) (expirationMillis / 1000)); // 초 단위로 변환
        cookie.setAttribute("SameSite", "Lax"); // CSRF 방지 (스프링 부트 2.6+)

        response.addCookie(cookie);
        log.info(
            "Added {} cookie with expiration: {} seconds", cookieName, expirationMillis / 1000
        );
    }

    private void deleteTokenCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        log.debug("Deleted cookie: {}", cookieName);
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String generateToken(UUID userId, long expirationMillis, String tokenType) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationMillis);

        String token = Jwts.builder()
            .claim("userId", userId.toString())
            .claim("tokenType", tokenType)
            .issuedAt(now)
            .expiration(expiration)
            .signWith(getSigningKey())
            .compact();

        log.debug("Generated {} token with expiration: {}", tokenType, expiration);
        return token;
    }
}
