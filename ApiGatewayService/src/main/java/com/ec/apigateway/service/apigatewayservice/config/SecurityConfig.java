package com.ec.apigateway.service.apigatewayservice.config;

import com.ec.apigateway.service.apigatewayservice.helper.CustomAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcReactiveOAuth2UserService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.core.convert.converter.Converter;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final CustomAuthenticationEntryPoint customEntryPoint;

    public SecurityConfig(CustomAuthenticationEntryPoint customEntryPoint) {
        this.customEntryPoint = customEntryPoint;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {

        // 🔐 JWT → Authorities converter (FOR API CALLS)
        Converter<Jwt, Mono<JwtAuthenticationToken>> jwtAuthConverter =
                jwt -> {

                    List<GrantedAuthority> authorities = new ArrayList<>();

                    // Default scopes (SCOPE_*)
                    JwtGrantedAuthoritiesConverter scopesConverter =
                            new JwtGrantedAuthoritiesConverter();
                    authorities.addAll(scopesConverter.convert(jwt));

                    // 👇 myclaim → ROLE_*
                    List<String> roles = jwt.getClaimAsStringList("myclaim");
                    if (roles != null) {
                        roles.forEach(role ->
                                authorities.add(
                                        new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())
                                )
                        );
                    }

                    return Mono.just(new JwtAuthenticationToken(jwt, authorities));
                };

        httpSecurity
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(
                                "/auth/login",
                                "/api-docs/**",
                                "/swagger-ui.html",
                                "/swagger/**",
                                "/swagger-ui/**",
                                "/webjars/**",
                                "/actuator/**"
                        ).permitAll()
                        .anyExchange().authenticated()
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(this.customEntryPoint)
                )
                // 🌐 Browser login (OIDC)
                .oauth2Login(oauth2 ->
                        oauth2.authenticationSuccessHandler(
                                new RedirectServerAuthenticationSuccessHandler("/auth/login")
                        )
                )
                // 🔐 API security (Bearer token)
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt ->
                                jwt.jwtAuthenticationConverter(jwtAuthConverter)
                        )
                )
                .csrf(ServerHttpSecurity.CsrfSpec::disable);

        return httpSecurity.build();
    }

    // 🌐 Add ROLES during OIDC login (FOR /auth/login response)
    @Bean
    public ReactiveOAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {

        OidcReactiveOAuth2UserService delegate =
                new OidcReactiveOAuth2UserService();

        return userRequest ->
                delegate.loadUser(userRequest).map(oidcUser -> {

                    Collection<GrantedAuthority> mappedAuthorities =
                            new ArrayList<>(oidcUser.getAuthorities());

                    // 👇 myclaim → ROLE_*
                    List<String> myClaims = oidcUser.getAttribute("myclaim");
                    if (myClaims != null) {
                        myClaims.forEach(role ->
                                mappedAuthorities.add(
                                        new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())
                                )
                        );
                    }

                    return new DefaultOidcUser(
                            mappedAuthorities,
                            oidcUser.getIdToken(),
                            oidcUser.getUserInfo()
                    );
                });
    }
}