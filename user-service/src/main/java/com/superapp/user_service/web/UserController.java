package com.superapp.user_service.web;

import com.superapp.user_service.domain.User;
import com.superapp.user_service.keycloak.KeycloakAdminService;
import com.superapp.user_service.repo.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository repo;
    private final KeycloakAdminService kc;

    public UserController(UserRepository repo, KeycloakAdminService kc) {
        this.repo = repo;
        this.kc = kc;
    }

    @GetMapping
    public List<User> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public User one(@PathVariable String id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public User create(@RequestBody User u) {
        return repo.findByEmail(u.getEmail()).orElseGet(() -> {
            // 1) Create user in Keycloak (if not exists) and assign USER role
            String keycloakId = kc.ensureUserWithRole(u.getEmail(), u.getName(), "USER");
            u.setKeycloakSub(keycloakId);
            // 2) Save app profile
            return repo.save(u);
        });
    }

    @PutMapping("/{id}")
    public User update(@PathVariable String id, @RequestBody User u) {
        User cur = repo.findById(id).orElseThrow();
        cur.setName(u.getName());
        cur.setAge(u.getAge());
        // email changes should be done in Keycloak; keep them in sync if you allow it
        return repo.save(cur);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        repo.deleteById(id);
    }
}