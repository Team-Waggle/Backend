package com.waggle.domain.user.service;

import com.waggle.domain.user.entity.User;
import com.waggle.domain.user.repository.UserRepository;
import com.waggle.global.exception.JwtTokenException;
import com.waggle.global.response.ApiStatus;
import com.waggle.global.secure.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public User getCurrentUser() {
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest()
                .getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            throw new JwtTokenException(ApiStatus._INVALID_ACCESS_TOKEN);
        }

        token = token.substring(7);

        String userId = jwtUtil.getUserIdFromToken(token);
        if (userId == null) {
            throw new JwtTokenException(ApiStatus._INVALID_ACCESS_TOKEN);
        }

        return userRepository.findByUserId(UUID.fromString(userId))
                .orElseThrow(() -> new JwtTokenException(ApiStatus._INVALID_ACCESS_TOKEN));
    }
}
