package com.superapp.user_service.keycloak;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "keycloak")
public class KeycloakAdminProperties {
    private String url;
    private String realm;
    private String adminRealm;
    private String adminClientId;
    private String adminUsername;
    private String adminPassword;
}
