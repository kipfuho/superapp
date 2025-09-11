package com.superapp.branch_service.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.superapp.branch_service.domain.TableArea;

public interface TableAreaRepo extends MongoRepository<TableArea, String> {
    List<TableArea> findByBranchIdAndActiveOrderBySortAsc(String branchId, boolean active);

    Boolean existsByName(String name);
}
