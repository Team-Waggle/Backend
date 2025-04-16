package com.waggle.domain.auth.service;

import com.waggle.domain.auth.dto.AccessTokenVo;
import com.waggle.domain.user.entity.User;

public interface AuthService {

    User getCurrentUser();

    AccessTokenVo reissueAccessToken(String refreshToken);

    AccessTokenVo exchangeTemporaryToken(String temporaryToken);

    void deleteRefreshToken(String refreshToken);
}
