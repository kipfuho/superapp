package com.superapp.branch_service.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.superapp.branch_service.domain.MenuCategory;

public interface MenuCategoryRepo extends MongoRepository<MenuCategory, String> {
    List<MenuCategory> findByBranchIdAndActiveOrderBySortAsc(String branchId, boolean active);
}
