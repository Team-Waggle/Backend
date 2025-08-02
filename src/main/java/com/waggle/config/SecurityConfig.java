package com.waggle.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.waggle.global.response.ApiStatus;
import com.waggle.global.response.ErrorResponse;
import com.waggle.global.security.filter.JwtAuthenticationFilter;
import com.waggle.global.security.oauth2.handler.OAuth2LoginFailureHandler;
import com.waggle.global.security.oauth2.handler.OAuth2LoginSuccessHandler;
import com.waggle.global.security.oauth2.service.CustomOAuth2UserService;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Slf4j
@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final ObjectMapper objectMapper;

    CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedHeaders(Collections.singletonList("*"));
            config.setAllowedMethods(Collections.singletonList("*"));
            config.setAllowedOriginPatterns(Collections.singletonList("*"));
            config.setAllowCredentials(true);
            return config;
        };
    }

    @Bean
    public AuthenticationEntryPoint jwtAuthenticationEntryPoint() {
        return (request, response, authException) -> {
            log.warn("Unauthorized access attempt: {}", authException.getMessage());

            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(ApiStatus._UNAUTHORIZED.getHttpStatus().value());

            ErrorResponse errorResponse = new ErrorResponse(
                ApiStatus._UNAUTHORIZED.getCode(),
                ApiStatus._UNAUTHORIZED.getMessage()
            );

            objectMapper.writeValue(response.getWriter(), errorResponse);
        };
    }

    @Bean
    public AccessDeniedHandler jwtAccessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            log.warn("Access denied: {}", accessDeniedException.getMessage());

            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(ApiStatus._FORBIDDEN.getHttpStatus().value());

            ErrorResponse errorResponse = new ErrorResponse(
                ApiStatus._FORBIDDEN.getCode(),
                ApiStatus._FORBIDDEN.getMessage()
            );

            objectMapper.writeValue(response.getWriter(), errorResponse);
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.
            httpBasic(HttpBasicConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .cors(configurer -> configurer.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(configurer -> configurer
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html").permitAll()
                .requestMatchers("/v3/api-docs/**", "/api-docs/**").permitAll()
                .requestMatchers("/swagger-resources/**").permitAll()
                .requestMatchers("/webjars/**").permitAll()
                .requestMatchers("/", "/login", "/error", "/favicon.ico").permitAll()
                .requestMatchers("/oauth2/**", "/login/oauth2/**").permitAll()
                .requestMatchers("/api/v2/auth/**").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterAfter(jwtAuthenticationFilter, OAuth2LoginAuthenticationFilter.class)
            .oauth2Login(oauth -> oauth
//                .authorizationEndpoint(authorization ->
//                    authorization.baseUri("/oauth2/authorization"))
//                .redirectionEndpoint(redirection ->
//                    redirection.baseUri("/oauth2/callback/*"))

                    // 사용자 정보 처리
                    .userInfoEndpoint(userInfo ->
                        userInfo.userService(customOAuth2UserService))

                    // 성공/실패 핸들러
                    .successHandler(oAuth2LoginSuccessHandler)
                    .failureHandler(oAuth2LoginFailureHandler)

                    .permitAll()
            )
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint(jwtAuthenticationEntryPoint())
                .accessDeniedHandler(jwtAccessDeniedHandler())
            )
            .build();
    }
}
