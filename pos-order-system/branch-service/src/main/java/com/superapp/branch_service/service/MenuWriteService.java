package com.superapp.branch_service.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.superapp.branch_service.domain.Dish;
import com.superapp.branch_service.domain.MenuCategory;
import com.superapp.branch_service.domain.ModifierGroup;
import com.superapp.branch_service.domain.ModifierOption;
import com.superapp.branch_service.repo.DishRepo;
import com.superapp.branch_service.repo.MenuCategoryRepo;
import com.superapp.branch_service.repo.ModifierGroupRepo;
import com.superapp.branch_service.repo.ModifierOptionRepo;
import com.superapp.branch_service.web.dto.CategoryDtos.CreateCategoryReq;
import com.superapp.branch_service.web.dto.CategoryDtos.UpdateCategoryReq;
import com.superapp.branch_service.web.dto.DishDtos.CreateDishReq;
import com.superapp.branch_service.web.dto.DishDtos.UpdateDishReq;
import com.superapp.branch_service.web.dto.ModifierDtos.CreateGroupReq;
import com.superapp.branch_service.web.dto.ModifierDtos.CreateOptionReq;
import com.superapp.branch_service.web.dto.ModifierDtos.UpdateGroupReq;
import com.superapp.branch_service.web.dto.ModifierDtos.UpdateOptionReq;
import com.superapp.branch_service.web.mapper.MenuMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuWriteService {
    private final MenuCategoryRepo categoryRepo;
    private final DishRepo dishRepo;
    private final ModifierGroupRepo groupRepo;
    private final ModifierOptionRepo optionRepo;
    private final CacheManager cacheManager;
    private final MenuMapper mapper;

    private void bust(String branchId) {
        Optional.ofNullable(cacheManager.getCache("menuByBranch")).ifPresent(c -> c.evict(branchId));
    }

    // Category
    public MenuCategory createCategory(CreateCategoryReq req) {
        MenuCategory c = mapper.toCategory(req);
        MenuCategory saved = categoryRepo.save(c);
        bust(req.branchId());
        return saved;
    }

    public MenuCategory updateCategory(String id, UpdateCategoryReq req) {
        MenuCategory c = categoryRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Category not found"));
        mapper.updateCategory(req, c);
        MenuCategory saved = categoryRepo.save(c);
        bust(c.getBranchId());
        return saved;
    }

    // Dish
    public Dish createDish(CreateDishReq req) {
        Dish d = mapper.toDish(req);
        if (d.getTagIds() == null)
            d.setTagIds(List.of());
        Dish saved = dishRepo.save(d);
        bust(req.branchId());
        return saved;
    }

    public Dish updateDish(String id, UpdateDishReq req) {
        Dish d = dishRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Dish not found"));
        mapper.updateDish(req, d);
        Dish saved = dishRepo.save(d);
        bust(d.getBranchId());
        return saved;
    }

    // Modifier group
    public ModifierGroup createGroup(CreateGroupReq req) {
        ModifierGroup g = mapper.toGroup(req);
        ModifierGroup saved = groupRepo.save(g);
        bust(req.branchId());
        return saved;
    }

    public ModifierGroup updateGroup(String id, UpdateGroupReq req) {
        ModifierGroup g = groupRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Group not found"));
        mapper.updateGroup(req, g);
        ModifierGroup saved = groupRepo.save(g);
        bust(g.getBranchId());
        return saved;
    }

    // Option
    public ModifierOption createOption(CreateOptionReq req) {
        ModifierOption o = mapper.toOption(req);
        ModifierOption saved = optionRepo.save(o);
        ModifierGroup group = groupRepo.findById(req.groupId()).orElse(null);
        if (group != null)
            bust(group.getBranchId());
        return saved;
    }

    public ModifierOption updateOption(String id, UpdateOptionReq req) {
        ModifierOption o = optionRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Option not found"));
        mapper.updateOption(req, o);
        ModifierOption saved = optionRepo.save(o);
        ModifierGroup group = groupRepo.findById(o.getGroupId()).orElse(null);
        if (group != null)
            bust(group.getBranchId());
        return saved;
    }
}
