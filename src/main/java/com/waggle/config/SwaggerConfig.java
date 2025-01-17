package com.waggle.config;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

    @Value("${MAIN_URL}")
    private String mainUrl;

    @Value("${NAVER_REDIRECT_URI}")
    private String naverRedirectUri;

    @Value("${NAVER_TOKEN_URI}")
    private String naverTokenUri;
    
    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("Waggle API Document")
                .version("v0.0.1")
                .description("Waggle 프로젝트의 API 명세서입니다.");

        // OAuth2 보안 스키마 정의
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.OAUTH2)
                .flows(new OAuthFlows()
                        .authorizationCode(new OAuthFlow()
                                .authorizationUrl(mainUrl + naverTokenUri)
                                .tokenUrl(mainUrl + naverRedirectUri)
                                .scopes(new Scopes()
                                        .addString("name", "이름 정보")
                                        .addString("email", "이메일 정보")
                                        .addString("profile_image", "프로필 이미지 정보")
                                        .addString("birthyear", "생년 정보")
                                        .addString("birthday", "생일 정보")
                                        .addString("mobile", "휴대폰 번호 정보")
                                        .addString("nickname", "닉네임 정보")
                                        .addString("gender", "성별 정보")
                                )
                        )
                );

        // JWT Bearer 인증 스키마
        SecurityScheme bearerScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .name("JWT");

        // 보안 요구사항 정의
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("OAuth2");
        SecurityRequirement jwtSecurityRequirement = new SecurityRequirement()
                .addList("JWT");

        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("OAuth2", securityScheme)
                        .addSecuritySchemes("JWT", bearerScheme))
                .info(info);
    }

    @Bean
    public OpenApiCustomizer sortSchemasAlphabetically() {
        return openApi -> {
            Map<String, Schema> schemas = openApi.getComponents().getSchemas();
            openApi.getComponents().setSchemas(new TreeMap<>(schemas));
        };
    }
}
