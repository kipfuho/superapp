package com.superapp.branch_service.repo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.superapp.branch_service.domain.BranchMenuPublish;

public interface BranchMenuPublishRepo extends MongoRepository<BranchMenuPublish, String> {
    Optional<BranchMenuPublish> findFirstByBranchIdOrderByVersionDesc(String branchId);
}