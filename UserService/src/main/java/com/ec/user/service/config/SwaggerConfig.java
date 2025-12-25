package com.ec.user.service.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
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
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://apache.org/licenses/LICENSE-2.0")
                        )
                )
                .servers(List.of(
                        new Server().url("http://localhost:8081").description("Local Server"),
                        new Server().url("https://api.ecommercestore.com").description("Production Server")
                ))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT"))
                );
    }
}
