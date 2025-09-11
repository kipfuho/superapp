package com.superapp.branch_service.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.superapp.branch_service.domain.Dish;
import com.superapp.branch_service.domain.MenuCategory;
import com.superapp.branch_service.domain.ModifierGroup;
import com.superapp.branch_service.domain.ModifierOption;
import com.superapp.branch_service.web.dto.CategoryDtos.CreateCategoryReq;
import com.superapp.branch_service.web.dto.CategoryDtos.UpdateCategoryReq;
import com.superapp.branch_service.web.dto.DishDtos.CreateDishReq;
import com.superapp.branch_service.web.dto.DishDtos.UpdateDishReq;
import com.superapp.branch_service.web.dto.ModifierDtos.CreateGroupReq;
import com.superapp.branch_service.web.dto.ModifierDtos.CreateOptionReq;
import com.superapp.branch_service.web.dto.ModifierDtos.UpdateGroupReq;
import com.superapp.branch_service.web.dto.ModifierDtos.UpdateOptionReq;

@Mapper(componentModel = "spring")
public interface MenuMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    Dish toDish(CreateDishReq req);

    @Mapping(target = "branchId", ignore = true)
    void updateDish(UpdateDishReq req, @MappingTarget Dish target);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    MenuCategory toCategory(CreateCategoryReq req);

    @Mapping(target = "branchId", ignore = true)
    void updateCategory(UpdateCategoryReq req, @MappingTarget MenuCategory target);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    ModifierGroup toGroup(CreateGroupReq req);

    @Mapping(target = "branchId", ignore = true)
    void updateGroup(UpdateGroupReq req, @MappingTarget ModifierGroup target);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    ModifierOption toOption(CreateOptionReq req);

    @Mapping(target = "branchId", ignore = true)
    void updateOption(UpdateOptionReq req, @MappingTarget ModifierOption target);
}
