package com.superapp.menu_service.repo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.superapp.menu_service.domain.Branch;

public interface BranchRepo extends MongoRepository<Branch, String> {
    Optional<Branch> findByCode(String code);
}
