package com.superapp.branch_service.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.superapp.branch_service.domain.DiningTable;

public interface DiningTableRepo extends MongoRepository<DiningTable, String> {
    Optional<DiningTable> findByCode(String code);

    List<DiningTable> findByBranchIdAndActiveOrderBySortAsc(String branchId, boolean active);

    Boolean existsByCode(String code);
}
