package com.superapp.user_service.web;

import com.superapp.user_service.entity.User;
import com.superapp.user_service.keycloak.KeycloakAdminService;
import com.superapp.user_service.repo.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository repo;
    private final KeycloakAdminService kc;

    public AuthController(UserRepository repo, KeycloakAdminService kc) {
        this.repo = repo;
        this.kc = kc;
    }

    /** Register new user in Keycloak and app DB */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User register(@RequestBody User u) {
        return repo.findByEmail(u.getEmail()).orElseGet(() -> {
            String keycloakId = kc.ensureUserWithRole(u.getEmail(), u.getName(), "USER");
            u.setKeycloakSub(keycloakId);
            return repo.save(u);
        });
    }

    /**
     * Login: request token from Keycloak using Resource Owner Password Credentials
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> req) {
        String email = req.get("email");
        String password = req.get("password");
        if (email == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email and password required"));
        }

        try {
            Map<String, Object> tokenResponse = kc.userToken(email, password);
            return ResponseEntity.ok(tokenResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid credentials"));
        }
    }
}
