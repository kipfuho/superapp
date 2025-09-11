package com.superapp.branch_service.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.superapp.branch_service.domain.Dish;
import com.superapp.branch_service.domain.MenuCategory;
import com.superapp.branch_service.domain.ModifierGroup;
import com.superapp.branch_service.domain.ModifierOption;
import com.superapp.branch_service.repo.DishRepo;
import com.superapp.branch_service.repo.MenuCategoryRepo;
import com.superapp.branch_service.repo.ModifierGroupRepo;
import com.superapp.branch_service.repo.ModifierOptionRepo;
import com.superapp.branch_service.util.PageParams;
import com.superapp.branch_service.util.Paged;

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
        List<MenuCategory> categories = categoryRepo.findByBranchIdAndActiveOrderBySortAsc(branchId, true);
        List<Dish> dishes = dishRepo.findByBranchIdAndActiveOrderBySortAsc(branchId, true);
        List<ModifierGroup> groups = groupRepo.findByBranchIdOrderBySortAsc(branchId);
        List<String> groupIds = groups.stream().map(ModifierGroup::getId).toList();
        Map<String, List<ModifierOption>> optionsByGroup = optionRepo.findAllById(groupIds).isEmpty() ? Map.of()
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
        PageParams params = PageParams.of(page, size, sort, dir);
        Pageable pageable = params.toPageable();
        Page<Dish> result = (categoryId == null || categoryId.isBlank())
                ? dishRepo.findByBranchIdAndActive(branchId, true, pageable)
                : dishRepo.findByBranchIdAndCategoryIdAndActive(branchId, categoryId, true, pageable);
        return Paged.fromPage(result, params.sortDescriptor());
    }
}
