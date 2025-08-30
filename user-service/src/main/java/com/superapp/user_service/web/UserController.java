package com.superapp.user_service.web;

import com.superapp.user_service.entity.User;
import com.superapp.user_service.repo.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository repo;

    public UserController(UserRepository repo) {
        this.repo = repo;
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
        return repo.save(u);
    }

    @PutMapping("/{id}")
    public User update(@PathVariable String id, @RequestBody User u) {
        u.setId(id);
        return repo.save(u);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        repo.deleteById(id);
    }
}