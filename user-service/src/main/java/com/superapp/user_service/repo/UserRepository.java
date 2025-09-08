package com.superapp.user_service.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.superapp.user_service.domain.User;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
}