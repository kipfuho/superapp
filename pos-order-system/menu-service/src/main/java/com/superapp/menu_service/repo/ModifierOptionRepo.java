package com.superapp.menu_service.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.superapp.menu_service.domain.ModifierOption;

public interface ModifierOptionRepo extends MongoRepository<ModifierOption, String> {
    List<ModifierOption> findByGroupIdAndIsActiveOrderBySortAsc(String groupId, boolean isActive);
}
