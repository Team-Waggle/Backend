package com.waggle.domain.auth.service;

import com.waggle.domain.auth.dto.AccessTokenVo;

public interface AuthService {
    AccessTokenVo reissueAccessToken(String refreshToken);
    AccessTokenVo exchangeTemporaryToken(String temporaryToken);
    void deleteRefreshToken(String refreshToken);
}
