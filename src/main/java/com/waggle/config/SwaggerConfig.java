package com.waggle.config;

import java.util.Arrays;

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
                                .authorizationUrl("http://localhost:8080/oauth2/authorization/google")  // OAuth2 인증 URL
                                .tokenUrl("http://localhost:8080/login/oauth2/code/google")           // 토큰 URL
                                .scopes(new Scopes()
                                        .addString("profile", "프로필 정보")
                                        .addString("email", "이메일 정보")
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
                .security(Arrays.asList(securityRequirement, jwtSecurityRequirement))
                .info(info);
    }
}
