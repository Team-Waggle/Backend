package com.waggle.apis.auth.dto;

public interface OAuth2UserInfo {
    String getProviderId();
    String getProvider();
    String getName();
    String getEmail();
}
