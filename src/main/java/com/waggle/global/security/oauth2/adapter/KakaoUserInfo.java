package com.waggle.global.security.oauth2.adapter;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;
    private final Map<String, Object> account;
    private final Map<String, Object> profile;

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
        this.account = (Map<String, Object>) attributes.get("kakao_account");
        this.profile = account != null ?
            (Map<String, Object>) account.get("profile") : null;
    }

    @Override
    public String getProviderId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getName() {
        return profile != null ? (String) profile.get("nickname") : null;
    }

    @Override
    public String getEmail() {
        return account != null ? (String) account.get("email") : null;
    }

    @Override
    public String getProfileImage() {
        return profile != null ? (String) profile.get("profile_image_url") : null;
    }
}
