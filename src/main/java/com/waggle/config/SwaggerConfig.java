package com.waggle.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class SwaggerConfig {

    @Value("${NAVER_REDIRECT_URI}")
    private String naverRedirectUri;

    @Value("${NAVER_TOKEN_URI}")
    private String naverTokenUri;

    @Value("${LOCAL_FULL_URL}")
    private String localServerUrl;

    @Value("${PROD_HTTPS_FULL_URL}")
    private String productionServerUrl;

    private OpenAPI createOpenAPI(String serverUrl) {
        Info info = new Info()
            .title("Waggle API Document")
            .version("v0.0.1")
            .description("Waggle 프로젝트의 API 명세서입니다.");

        // OAuth2 보안 스키마 정의
        SecurityScheme securityScheme = new SecurityScheme()
            .type(SecurityScheme.Type.OAUTH2)
            .flows(new OAuthFlows()
                .authorizationCode(new OAuthFlow()
                    .authorizationUrl(serverUrl + naverTokenUri)
                    .tokenUrl(serverUrl + naverRedirectUri)
                    .scopes(new Scopes()
                        .addString("name", "이름 정보")
                        .addString("email", "이메일 정보")
                        .addString("profile_image", "프로필 이미지 정보")
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
            .info(info)
            .addServersItem(new Server().url(serverUrl));
    }

    @Bean
    @Profile("local")
    public OpenAPI openAPIForLocal() {
        return createOpenAPI(localServerUrl);
    }

    @Bean
    @Profile("prod")
    public OpenAPI openAPIForProduction() {
        return createOpenAPI(productionServerUrl);
    }

    @Bean
    public OpenApiCustomizer sortSchemasAlphabetically() {
        return openApi -> {
            Map<String, Schema> schemas = openApi.getComponents().getSchemas();
            openApi.getComponents().setSchemas(new TreeMap<>(schemas));
        };
    }

    @Bean
    public OpenApiCustomizer configureTags() {
        return openApi -> {
            openApi.setTags(List.of(
                new io.swagger.v3.oas.models.tags.Tag().name("인증"),
                new io.swagger.v3.oas.models.tags.Tag().name("사용자"),
                new io.swagger.v3.oas.models.tags.Tag().name("프로젝트 모집 게시글"),
                new io.swagger.v3.oas.models.tags.Tag().name("프로젝트 지원"),
                new io.swagger.v3.oas.models.tags.Tag().name("프로젝트 멤버"),
                new io.swagger.v3.oas.models.tags.Tag().name("프로젝트 북마크"),
                new io.swagger.v3.oas.models.tags.Tag().name("참조 데이터"),
                new io.swagger.v3.oas.models.tags.Tag().name("인앱 알림")
            ));
        };
    }
}
