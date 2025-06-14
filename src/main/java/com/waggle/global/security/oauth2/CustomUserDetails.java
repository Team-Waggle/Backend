package com.waggle.global.security.oauth2;

import com.waggle.domain.user.entity.User;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

@RequiredArgsConstructor
public class CustomUserDetails implements OAuth2User, UserDetails {

    private final OAuth2User oAuth2User;

    @Getter
    private final User user;

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User != null ? oAuth2User.getAttributes() : Map.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User != null ? oAuth2User.getAuthorities() : Collections.emptyList();
    }

    @Override
    public String getName() {
        return user.getName();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
