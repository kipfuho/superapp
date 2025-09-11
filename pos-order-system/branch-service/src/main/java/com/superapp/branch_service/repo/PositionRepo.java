package com.superapp.branch_service.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.superapp.branch_service.domain.BranchPosition;

public interface PositionRepo extends MongoRepository<BranchPosition, String> {
    List<BranchPosition> findByBranchIdOrBranchIdIsNull(String branchId);
}