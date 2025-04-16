package com.waggle.global.secure.oauth2.adapter;

public interface OAuth2UserInfo {

    String getProviderId();

    String getProvider();

    String getName();

    String getEmail();

    String getProfileImage();
}
