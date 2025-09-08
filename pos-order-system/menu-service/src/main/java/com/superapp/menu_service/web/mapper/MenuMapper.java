package com.superapp.menu_service.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.superapp.menu_service.domain.Dish;
import com.superapp.menu_service.domain.MenuCategory;
import com.superapp.menu_service.domain.ModifierGroup;
import com.superapp.menu_service.domain.ModifierOption;
import com.superapp.menu_service.web.dto.CategoryDtos.CreateCategoryReq;
import com.superapp.menu_service.web.dto.CategoryDtos.UpdateCategoryReq;
import com.superapp.menu_service.web.dto.DishDtos.CreateDishReq;
import com.superapp.menu_service.web.dto.DishDtos.UpdateDishReq;
import com.superapp.menu_service.web.dto.ModifierDtos.CreateGroupReq;
import com.superapp.menu_service.web.dto.ModifierDtos.CreateOptionReq;
import com.superapp.menu_service.web.dto.ModifierDtos.UpdateGroupReq;
import com.superapp.menu_service.web.dto.ModifierDtos.UpdateOptionReq;

@Mapper(componentModel = "spring")
public interface MenuMapper {
    Dish toDish(CreateDishReq req);

    void updateDish(UpdateDishReq req, @MappingTarget Dish target);

    MenuCategory toCategory(CreateCategoryReq req);

    void updateCategory(UpdateCategoryReq req, @MappingTarget MenuCategory target);

    ModifierGroup toGroup(CreateGroupReq req);

    void updateGroup(UpdateGroupReq req, @MappingTarget ModifierGroup target);

    ModifierOption toOption(CreateOptionReq req);

    void updateOption(UpdateOptionReq req, @MappingTarget ModifierOption target);
}
