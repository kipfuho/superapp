package com.superapp.menu_service.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.superapp.menu_service.domain.ModifierGroup;

public interface ModifierGroupRepo extends MongoRepository<ModifierGroup, String> {
    List<ModifierGroup> findByBranchIdOrderBySortAsc(String branchId);
}
