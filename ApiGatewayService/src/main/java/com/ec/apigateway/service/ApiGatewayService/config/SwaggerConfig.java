package com.ec.apigateway.service.ApiGatewayService.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        Scopes oauthScopes = new Scopes()
                .addString("openid", "Identity info")
                .addString("profile", "User details")
                .addString("email", "Email access")
                .addString("offline_access", "Refresh token")
                .addString("internal", "Internal scope");

        OAuthFlow authFlow = new OAuthFlow()
                .authorizationUrl("https://trial-5285711.okta.com/oauth2/default/v1/authorize")
                .tokenUrl("https://trial-5285711.okta.com/oauth2/default/v1/token");

        authFlow.setScopes(oauthScopes);
        authFlow.addExtension("x-use-pkce", true);

        return new OpenAPI()
                .info(new Info()
                        .title("E-commerce Store API Documentation")
                        .version("v1.0.0")
                        .description("Advanced OpenAPI Implementation for E-commerce Store")
                        .contact(new Contact()
                                .name("Mukul Sharma")
                                .email("support@ecommercestore.com")
                                .url("https://ecommercestore.com")
                        )
                )
                .servers(List.of(
                        new Server().url("http://localhost:8086").description("API Gateway Server")
                ))
                .addSecurityItem(new SecurityRequirement().addList("Okta_OAuth2").addList("Bearer_Auth"))
                .components(new Components()
                        .addSecuritySchemes("Bearer_Auth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT"))
                        .addSecuritySchemes("Okta_OAuth2",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.OAUTH2)
                                        .flows(new OAuthFlows()
                                                .authorizationCode(authFlow)
                                        )
                        )
                );
    }
}