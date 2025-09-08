package com.superapp.menu_service.repo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.superapp.menu_service.domain.BranchMenuPublish;

public interface BranchMenuPublishRepo extends MongoRepository<BranchMenuPublish, String> {
    Optional<BranchMenuPublish> findFirstByBranchIdOrderByVersionDesc(String branchId);
}