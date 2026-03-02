package com.homeBanking.gateway.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Value("${dh.keycloak.serverUrl}")
    private String serverUrl;
    public static final String ALLOWED_ORIGINS = "http://localhost:3000";
    public static final String MAX_AGE = "3600";
    public static final String ALLOW_CREDENTIALS = "false";
    public static final String ALLOW_METHODS = "GET, POST, PUT, PATCH, DELETE, OPTIONS";
    public static final String COMMA_SEPARATOR = ",";

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        http.authorizeExchange(
             authorizeExchangeSpec -> authorizeExchangeSpec
                .anyExchange().permitAll()
                )
               .csrf(csrfSpec -> csrfSpec.disable())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                //.jwkSetUri("http://keycloak:8080/realms/dh-money-users/protocol/openid-connect/certs")
                                .jwkSetUri(serverUrl)
                        )
                );

        return http.build();
    }
}
