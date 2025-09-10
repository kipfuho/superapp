package com.superapp.menu_service.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.superapp.menu_service.domain.Dish;

public interface DishRepo extends MongoRepository<Dish, String> {
    List<Dish> findByBranchIdAndActiveOrderBySortAsc(String branchId, boolean active);

    List<Dish> findByBranchIdAndCategoryIdAndActiveOrderBySortAsc(String branchId, String categoryId,
            boolean active);

    Page<Dish> findByBranchIdAndActive(String branchId, boolean active, Pageable pageable);

    Page<Dish> findByBranchIdAndCategoryIdAndActive(String branchId, String categoryId, boolean active,
            Pageable pageable);
}