package com.waggle.global.security.filter;

import com.waggle.domain.user.entity.User;
import com.waggle.domain.user.repository.UserRepository;
import com.waggle.global.security.jwt.JwtUtil;
import com.waggle.global.security.oauth2.UserPrincipal;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String token = jwtUtil.extractToken(request);

        if (token != null && jwtUtil.validateToken(token)
            && SecurityContextHolder.getContext().getAuthentication() == null
        ) {
            try {
                UUID userId = UUID.fromString(jwtUtil.getUserIdFromToken(token));
                User user = userRepository.findById(userId).orElse(null);

                if (user != null) {
                    UserPrincipal userPrincipal = new UserPrincipal(null, user);

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userPrincipal, null, userPrincipal.getAuthorities()
                    );

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.debug("JWT authentication successful for user: {}", userId);
                }
            } catch (Exception e) {
                log.error("JWT authentication failed: {}", e.getMessage());

            }
        }

        filterChain.doFilter(request, response);
    }
}
