// MVC style (if user-service uses spring-boot-starter-web)
package com.superapp.user_service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.superapp.user_service.keycloak.KeycloakRealmRoleConverter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // public endpoints
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/users/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth -> oauth.jwt(jwt -> jwt
                        .jwtAuthenticationConverter(KeycloakRealmRoleConverter.jwtAuthenticationConverter())));
        return http.build();
    }
}
