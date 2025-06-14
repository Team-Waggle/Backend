package com.waggle.global.security.oauth2.service;

import com.waggle.domain.user.entity.User;
import com.waggle.domain.user.repository.UserRepository;
import com.waggle.global.security.oauth2.CustomUserDetails;
import com.waggle.global.security.oauth2.adapter.GoogleUserInfo;
import com.waggle.global.security.oauth2.adapter.KakaoUserInfo;
import com.waggle.global.security.oauth2.adapter.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(
        OAuth2UserRequest oAuth2UserRequest
    ) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(oAuth2UserRequest);

        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo userInfo = switch (registrationId) {
            case "google" -> new GoogleUserInfo(oauth2User.getAttributes());
            case "kakao" -> new KakaoUserInfo(oauth2User.getAttributes());
            default ->
                throw new OAuth2AuthenticationException("Unsupported provider: " + registrationId);
        };

        String providerId = userInfo.getProviderId();
        User user = userRepository.findByProviderId(providerId).orElseGet(() ->
            userRepository.save(
                User.builder()
                    .provider(userInfo.getProvider())
                    .providerId(userInfo.getProviderId())
                    .profileImageUrl(userInfo.getProfileImage())
                    .name(userInfo.getName())
                    .email(userInfo.getEmail())
                    .build()
            )
        );

        return new CustomUserDetails(oauth2User, user);
    }
}
