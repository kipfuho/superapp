package com.superapp.booking_service.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    SecurityFilterChain security(HttpSecurity http,
            @Value("${cluster.secret}") String clusterSecret) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Allow all; our filter will protect only /queue/*/admin/**
                        .requestMatchers("/queue/*/admin/**").permitAll()
                        .anyRequest().permitAll())
                .addFilterBefore(new ClusterSecretFilter(clusterSecret),
                        org.springframework.security.web.authentication.AnonymousAuthenticationFilter.class);

        return http.build();
    }
}
