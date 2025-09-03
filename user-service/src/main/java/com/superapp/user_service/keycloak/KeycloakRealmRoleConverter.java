package com.superapp.user_service.keycloak;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.lang.NonNull;

import java.util.*;
import java.util.stream.Collectors;

public class KeycloakRealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    @Override
    @SuppressWarnings("unchecked")
    public Collection<GrantedAuthority> convert(@NonNull Jwt jwt) {
        // realm roles
        List<String> realmRoles = Optional.ofNullable(jwt.getClaimAsMap("realm_access"))
                .map(m -> (List<String>) m.get("roles"))
                .orElse(List.of());

        // // client roles (flatten across all clients)
        // Map<String, Object> resourceAccess = jwt.getClaimAsMap("resource_access");
        // List<String> clientRoles = resourceAccess == null ? List.of()
        // : resourceAccess.values().stream()
        // .filter(Map.class::isInstance)
        // .map(Map.class::cast)
        // .map(m -> (List<String>) m.get("roles"))
        // .filter(Objects::nonNull)
        // .flatMap(Collection::stream)
        // .toList();

        // combine, prefix ROLE_, and dedupe
        return realmRoles.stream()
                .filter(Objects::nonNull)
                .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
                .distinct()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public static JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakRealmRoleConverter());
        return converter;
    }
}