package com.superapp.user_service.keycloak;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class KeycloakAdminService {

    private final RestClient http; // synchronous client
    private final KeycloakAdminProperties cfg;
    private final String tokenUrl;

    public KeycloakAdminService(KeycloakAdminProperties cfg) {
        this.cfg = cfg;
        this.http = RestClient.builder()
                .baseUrl(cfg.getUrl())
                .build();
        this.tokenUrl = cfg.getUrl() + "/realms/" + cfg.getAdminRealm()
                + "/protocol/openid-connect/token";
    }

    /** Returns an admin access token (password grant, dev-only). */
    private String adminToken() {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "password");
        form.add("client_id", cfg.getAdminClientId());
        form.add("username", cfg.getAdminUsername());
        form.add("password", cfg.getAdminPassword());

        Map<String, Object> body = RestClient.create()
                .post()
                .uri(tokenUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {
                });

        if (body == null || body.get("access_token") == null) {
            throw new IllegalStateException("Failed to obtain Keycloak admin token");
        }
        return (String) body.get("access_token");
    }

    /** GET /admin/realms/{realm}/users?email=... */
    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> findUsersByEmail(String token, String realm, String email) {
        List<Map<String, Object>> users = http.get()
                .uri("/admin/realms/{realm}/users?email={email}", realm, email)
                .headers(h -> h.setBearerAuth(token))
                .retrieve()
                .body(List.class);
        return users != null ? users : Collections.emptyList();
    }

    /** POST /admin/realms/{realm}/users — returns Location with user id */
    private String createUser(String token, String realm, String email, String name) {
        Map<String, Object> payload = Map.of(
                "username", email,
                "email", email,
                "firstName", name,
                "enabled", true,
                "emailVerified", true);

        ResponseEntity<Void> resp = http.post()
                .uri("/admin/realms/{realm}/users", realm)
                .headers(h -> {
                    h.setBearerAuth(token);
                    h.setContentType(MediaType.APPLICATION_JSON);
                })
                .body(payload)
                .retrieve()
                .toEntity(Void.class);

        URI location = resp.getHeaders().getLocation();
        if (location != null) {
            // Location ends with .../users/{id}
            String path = location.getPath();
            int idx = path.lastIndexOf('/');
            if (idx >= 0 && idx + 1 < path.length()) {
                return path.substring(idx + 1);
            }
        }
        // Fallback: search by email
        return findUsersByEmail(token, realm, email)
                .stream()
                .findFirst()
                .map(u -> (String) u.get("id"))
                .orElseThrow(() -> new IllegalStateException("User created but id not found"));
    }

    /** GET /admin/realms/{realm}/roles/{role} */
    @SuppressWarnings("unchecked")
    private Map<String, Object> getRealmRole(String token, String realm, String roleName) {
        return http.get()
                .uri("/admin/realms/{realm}/roles/{role}", realm, roleName)
                .headers(h -> h.setBearerAuth(token))
                .retrieve()
                .body(Map.class);
    }

    /** POST /admin/realms/{realm}/users/{id}/role-mappings/realm */
    private void assignRealmRole(String token, String realm, String userId, Map<String, Object> fullRoleRep) {
        // Build a minimal but valid RoleRepresentation payload
        Map<String, Object> payload = Map.of(
                "id", fullRoleRep.get("id"),
                "name", fullRoleRep.get("name"),
                "composite", Boolean.FALSE,
                "clientRole", Boolean.FALSE,
                "containerId", realm);

        http.post()
                .uri("/admin/realms/{realm}/users/{id}/role-mappings/realm", realm, userId)
                .headers(h -> {
                    h.setBearerAuth(token);
                    h.setContentType(MediaType.APPLICATION_JSON);
                })
                .body(List.of(payload)) // MUST be an array
                .retrieve()
                .toBodilessEntity();
    }

    // --- List user's current realm roles
    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> getUserRealmRoles(String token, String realm, String userId) {
        List<Map<String, Object>> roles = http.get()
                .uri("/admin/realms/{realm}/users/{id}/role-mappings/realm", realm, userId)
                .headers(h -> h.setBearerAuth(token))
                .retrieve()
                .body(List.class);
        return roles != null ? roles : List.of();
    }

    // --- Check whether user already has a given realm role (by name)
    private boolean userHasRealmRole(String token, String realm, String userId, String roleName) {
        return getUserRealmRoles(token, realm, userId).stream()
                .map(m -> String.valueOf(m.get("name")))
                .anyMatch(roleName::equals);
    }

    /**
     * Ensure a user exists in the realm; creates if missing and ensures the role is
     * assigned.
     * Returns the Keycloak user id (UUID).
     */
    public String ensureUserWithRole(String email, String name, String role) {
        String token = adminToken();
        String realm = cfg.getRealm();

        // 1) Find or create user
        List<Map<String, Object>> found = findUsersByEmail(token, realm, email);
        String userId = found.isEmpty()
                ? createUser(token, realm, email, name)
                : String.valueOf(found.get(0).get("id"));

        // 2) Assign realm role if missing
        if (!userHasRealmRole(token, realm, userId, role)) {
            Map<String, Object> roleRep = getRealmRole(token, realm, role); // full rep has id+name
            if (roleRep == null || roleRep.get("id") == null) {
                throw new IllegalStateException("Realm role '" + role + "' not found in realm " + realm);
            }
            assignRealmRole(token, realm, userId, roleRep);
        }

        return userId;
    }

    /** User login: exchange username/password for token */
    public Map<String, Object> userToken(String username, String password) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "password");
        form.add("client_id", cfg.getAdminClientId()); // or a public client id
        form.add("username", username);
        form.add("password", password);

        return RestClient.create()
                .post()
                .uri(cfg.getUrl() + "/realms/" + cfg.getRealm() + "/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {
                });
    }
}
