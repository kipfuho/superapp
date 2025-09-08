package com.superapp.menu_service.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.superapp.menu_service.domain.Dish;

public interface DishRepo extends MongoRepository<Dish, String> {
    List<Dish> findByBranchIdAndIsActiveOrderBySortAsc(String branchId, boolean isActive);

    List<Dish> findByBranchIdAndCategoryIdAndIsActiveOrderBySortAsc(String branchId, String categoryId,
            boolean isActive);

    Page<Dish> findByBranchIdAndIsActive(String branchId, boolean isActive, Pageable pageable);

    Page<Dish> findByBranchIdAndCategoryIdAndIsActive(String branchId, String categoryId, boolean isActive,
            Pageable pageable);
}