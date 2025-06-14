package com.waggle.global.security.filter;

import com.waggle.domain.user.entity.User;
import com.waggle.domain.user.repository.UserRepository;
import com.waggle.global.exception.JwtTokenException;
import com.waggle.global.security.jwt.JwtUtil;
import com.waggle.global.security.oauth2.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String authorizationHeader = request.getHeader("Authorization");
            String token = jwtUtil.getTokenFromHeader(authorizationHeader);

            if (jwtUtil.validateToken(token)) {
                UUID userId = UUID.fromString(jwtUtil.getUserIdFromToken(token));
                User user = userRepository.findById(userId).orElse(null);

                if (user != null) {
                    CustomUserDetails customUserDetails = new CustomUserDetails(null, user);
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                        customUserDetails,
                        null,
                        Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
                    );

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (JwtTokenException e) {
            log.warn("JWT Token 예외: {}", e.getMessage());
        } catch (Exception e) {
            log.error("JWT 인증 과정에서 예상치 못한 예외 발생", e);
        }

        filterChain.doFilter(request, response);
    }
}
