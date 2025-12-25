package com.ec.user.service.config;

import com.ec.user.service.config.interceptor.RestTemplateInterceptor;
import com.ec.user.service.exceptions.customexceptions.CustomAccessDeniedHandler;
import com.ec.user.service.exceptions.customexceptions.CustomAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //This annotation apply security in methods(i.e,hasROLE)
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private  ClientRegistrationRepository clientRegistrationRepository;
    @Autowired
    private  OAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository;
    @Autowired
    private CustomAuthenticationEntryPoint customEntryPoint;
    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/users/basic/**",
                                "/api-docs/**",
                                "/swagger/**",
                                "/swagger-ui/**",
                                "/webjars/**",
                                "/actuator/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(customEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                )
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt ->
                                jwt.jwtAuthenticationConverter(userDefineJwtAuthenticationConverter())
                        )
                )
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        return http.build();
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {

        RestTemplate restTemplate = new RestTemplate();

        List<ClientHttpRequestInterceptor> interceptors=new ArrayList<>();

        interceptors.add(new RestTemplateInterceptor(manager(
                clientRegistrationRepository,
                oAuth2AuthorizedClientRepository
        )));

        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }

    //declare the bean of OAuth2AuthorizedClient manager
    @Bean
    public OAuth2AuthorizedClientManager manager(ClientRegistrationRepository clientRegistrationRepository, OAuth2AuthorizedClientRepository auth2AuthorizedClientRepository) {
        OAuth2AuthorizedClientProvider provider = OAuth2AuthorizedClientProviderBuilder.builder().clientCredentials().build();
        DefaultOAuth2AuthorizedClientManager defaultOAuth2AuthorizedClientManager = new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository, auth2AuthorizedClientRepository);
        defaultOAuth2AuthorizedClientManager.setAuthorizedClientProvider(provider);
        return defaultOAuth2AuthorizedClientManager;
    }

    // JWT → ROLE converter (MOST IMPORTANT)
    @Bean
    public JwtAuthenticationConverter userDefineJwtAuthenticationConverter() {

        JwtGrantedAuthoritiesConverter scopesConverter =
                new JwtGrantedAuthoritiesConverter(); // SCOPE_*

        JwtAuthenticationConverter jwtConverter =
                new JwtAuthenticationConverter();

        jwtConverter.setJwtGrantedAuthoritiesConverter(jwt -> {

            List<GrantedAuthority> authorities = new ArrayList<>();

            // 1️⃣ scopes → SCOPE_*
            authorities.addAll(scopesConverter.convert(jwt));

            // 2️⃣ roles → ROLE_*
            List<String> roles = jwt.getClaimAsStringList("roles");
            if (roles != null) {
                roles.forEach(role ->
                        authorities.add(
                                new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())
                        )
                );
            }

            return authorities;
        });

        return jwtConverter;
    }

}
