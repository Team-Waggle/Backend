package com.waggle.global.security.oauth2.adapter;

import java.util.Map;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class KakaoUserInfo implements OAuth2UserInfo {

    private Map<String, Object> attributes;

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
        Map<String, Object> profile = (Map<String, Object>) ((Map<String, Object>) attributes.get(
            "kakao_account")).get("profile");
        return (String) profile.get("nickname");
    }

    @Override
    public String getEmail() {
        return (String) ((Map<String, Object>) attributes.get("kakao_account")).get("email");
    }

    @Override
    public String getProfileImage() {
        Map<String, Object> profile = (Map<String, Object>) ((Map<String, Object>) attributes.get(
            "kakao_account")).get("profile");
        return (String) profile.get("profile_image_url");
    }
}
