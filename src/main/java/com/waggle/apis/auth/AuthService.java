package com.waggle.apis.auth;

import com.waggle.apis.auth.dto.AccessTokenResponse;
import com.waggle.apis.user.entity.User;

public interface AuthService {
    AccessTokenResponse reissueAccessToken(String refreshToken);
    User getCurrentUser();
}
