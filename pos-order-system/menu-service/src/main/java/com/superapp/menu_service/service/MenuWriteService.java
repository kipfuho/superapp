package com.superapp.menu_service.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.superapp.menu_service.domain.Dish;
import com.superapp.menu_service.domain.MenuCategory;
import com.superapp.menu_service.domain.ModifierGroup;
import com.superapp.menu_service.domain.ModifierOption;
import com.superapp.menu_service.repo.DishRepo;
import com.superapp.menu_service.repo.MenuCategoryRepo;
import com.superapp.menu_service.repo.ModifierGroupRepo;
import com.superapp.menu_service.repo.ModifierOptionRepo;
import com.superapp.menu_service.web.dto.CategoryDtos.CreateCategoryReq;
import com.superapp.menu_service.web.dto.CategoryDtos.UpdateCategoryReq;
import com.superapp.menu_service.web.dto.DishDtos.CreateDishReq;
import com.superapp.menu_service.web.dto.DishDtos.UpdateDishReq;
import com.superapp.menu_service.web.dto.ModifierDtos.CreateGroupReq;
import com.superapp.menu_service.web.dto.ModifierDtos.CreateOptionReq;
import com.superapp.menu_service.web.dto.ModifierDtos.UpdateGroupReq;
import com.superapp.menu_service.web.dto.ModifierDtos.UpdateOptionReq;
import com.superapp.menu_service.web.mapper.MenuMapper;

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
        var c = mapper.toCategory(req);
        var saved = categoryRepo.save(c);
        bust(req.branchId());
        return saved;
    }

    public MenuCategory updateCategory(String id, UpdateCategoryReq req) {
        var c = categoryRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Category not found"));
        mapper.updateCategory(req, c);
        var saved = categoryRepo.save(c);
        bust(c.getBranchId());
        return saved;
    }

    // Dish
    public Dish createDish(CreateDishReq req) {
        var d = mapper.toDish(req);
        if (d.getTagIds() == null)
            d.setTagIds(List.of());
        var saved = dishRepo.save(d);
        bust(req.branchId());
        return saved;
    }

    public Dish updateDish(String id, UpdateDishReq req) {
        var d = dishRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Dish not found"));
        mapper.updateDish(req, d);
        var saved = dishRepo.save(d);
        bust(d.getBranchId());
        return saved;
    }

    // Modifier group
    public ModifierGroup createGroup(CreateGroupReq req) {
        var g = mapper.toGroup(req);
        var saved = groupRepo.save(g);
        bust(req.branchId());
        return saved;
    }

    public ModifierGroup updateGroup(String id, UpdateGroupReq req) {
        var g = groupRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Group not found"));
        mapper.updateGroup(req, g);
        var saved = groupRepo.save(g);
        bust(g.getBranchId());
        return saved;
    }

    // Option
    public ModifierOption createOption(CreateOptionReq req) {
        var o = mapper.toOption(req);
        var saved = optionRepo.save(o);
        var group = groupRepo.findById(req.groupId()).orElse(null);
        if (group != null)
            bust(group.getBranchId());
        return saved;
    }

    public ModifierOption updateOption(String id, UpdateOptionReq req) {
        var o = optionRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Option not found"));
        mapper.updateOption(req, o);
        var saved = optionRepo.save(o);
        var group = groupRepo.findById(o.getGroupId()).orElse(null);
        if (group != null)
            bust(group.getBranchId());
        return saved;
    }
}
