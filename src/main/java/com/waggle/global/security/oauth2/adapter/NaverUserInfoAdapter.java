package com.waggle.global.security.oauth2.adapter;

import java.util.Map;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NaverUserInfoAdapter implements OAuth2UserInfo {

    private Map<String, Object> attributes;

    @Override
    public String getProviderId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getProfileImage() {
        return (String) attributes.get("profile_image");
    }
}
