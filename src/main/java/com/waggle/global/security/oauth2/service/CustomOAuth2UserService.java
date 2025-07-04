package com.waggle.global.security.oauth2.service;

import com.waggle.domain.user.entity.User;
import com.waggle.domain.user.repository.UserRepository;
import com.waggle.global.security.oauth2.UserPrincipal;
import com.waggle.global.security.oauth2.adapter.GoogleUserInfo;
import com.waggle.global.security.oauth2.adapter.KakaoUserInfo;
import com.waggle.global.security.oauth2.adapter.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
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

        User user = userRepository.findByProviderId(userInfo.getProviderId())
            .orElseGet(() -> {
                if (userRepository.existsByEmail(userInfo.getEmail())) {
                    throw new IllegalStateException("Email already in use: " + userInfo.getEmail());
                }

                return userRepository.save(
                    User.builder()
                        .provider(userInfo.getProvider())
                        .providerId(userInfo.getProviderId())
                        .name(userInfo.getName())
                        .email(userInfo.getEmail())
                        .profileImageUrl(userInfo.getProfileImage())
                        .build()
                );
            });

        return new UserPrincipal(oauth2User, user);
    }
}
