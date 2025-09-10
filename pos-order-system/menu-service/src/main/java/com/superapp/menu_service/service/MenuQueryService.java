package com.superapp.menu_service.service;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.superapp.menu_service.domain.Dish;
import com.superapp.menu_service.domain.ModifierGroup;
import com.superapp.menu_service.repo.DishRepo;
import com.superapp.menu_service.repo.MenuCategoryRepo;
import com.superapp.menu_service.repo.ModifierGroupRepo;
import com.superapp.menu_service.repo.ModifierOptionRepo;
import com.superapp.menu_service.util.PageParams;
import com.superapp.menu_service.util.Paged;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuQueryService {
    private final MenuCategoryRepo categoryRepo;
    private final DishRepo dishRepo;
    private final ModifierGroupRepo groupRepo;
    private final ModifierOptionRepo optionRepo;

    /**
     * Fast snapshot for branch menu.
     */
    @Cacheable(value = "menuByBranch", key = "#branchId")
    public Map<String, Object> getBranchMenu(String branchId) {
        var categories = categoryRepo.findByBranchIdAndActiveOrderBySortAsc(branchId, true);
        var dishes = dishRepo.findByBranchIdAndActiveOrderBySortAsc(branchId, true);
        var groups = groupRepo.findByBranchIdOrderBySortAsc(branchId);
        var groupIds = groups.stream().map(ModifierGroup::getId).toList();
        var optionsByGroup = optionRepo.findAllById(groupIds).isEmpty() ? Map.of()
                : groups.stream().collect(Collectors.toMap(ModifierGroup::getId,
                        g -> optionRepo.findByGroupIdAndActiveOrderBySortAsc(g.getId(), true)));

        return Map.of("categories", categories, "dishes", dishes, "groups", groups, "optionsByGroup", optionsByGroup);
    }

    /**
     * Paginated dishes for branch (optional category filter).
     */
    public Paged<Dish> pageDishes(String branchId,
            String categoryId,
            Integer page,
            Integer size,
            String sort,
            String dir) {
        var params = PageParams.of(page, size, sort, dir);
        var pageable = params.toPageable();
        Page<Dish> result = (categoryId == null || categoryId.isBlank())
                ? dishRepo.findByBranchIdAndActive(branchId, true, pageable)
                : dishRepo.findByBranchIdAndCategoryIdAndActive(branchId, categoryId, true, pageable);
        return Paged.fromPage(result, params.sortDescriptor());
    }
}
