package com.superapp.menu_service.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.superapp.menu_service.domain.MenuCategory;

public interface MenuCategoryRepo extends MongoRepository<MenuCategory, String> {
    List<MenuCategory> findByBranchIdAndActiveOrderBySortAsc(String branchId, boolean active);
}
