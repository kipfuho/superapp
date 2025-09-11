package com.superapp.branch_service.repo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.superapp.branch_service.domain.Brand;

public interface BrandRepo extends MongoRepository<Brand, String> {
    Optional<Brand> findByCode(String code);
}
