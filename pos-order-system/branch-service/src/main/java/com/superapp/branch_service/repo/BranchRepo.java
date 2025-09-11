package com.superapp.branch_service.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.superapp.branch_service.domain.Branch;

public interface BranchRepo extends MongoRepository<Branch, String> {
    Optional<Branch> findByCode(String code);

    List<Branch> findByBrandId(String brandId);
}
