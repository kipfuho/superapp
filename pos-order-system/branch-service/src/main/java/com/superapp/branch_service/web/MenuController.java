package com.superapp.branch_service.web;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.superapp.branch_service.domain.Dish;
import com.superapp.branch_service.domain.MenuCategory;
import com.superapp.branch_service.domain.ModifierGroup;
import com.superapp.branch_service.domain.ModifierOption;
import com.superapp.branch_service.service.MenuQueryService;
import com.superapp.branch_service.service.MenuWriteService;
import com.superapp.branch_service.service.PublishService;
import com.superapp.branch_service.util.Paged;
import com.superapp.branch_service.web.dto.CategoryDtos.CreateCategoryReq;
import com.superapp.branch_service.web.dto.CategoryDtos.UpdateCategoryReq;
import com.superapp.branch_service.web.dto.DishDtos.CreateDishReq;
import com.superapp.branch_service.web.dto.DishDtos.UpdateDishReq;
import com.superapp.branch_service.web.dto.ModifierDtos.CreateGroupReq;
import com.superapp.branch_service.web.dto.ModifierDtos.CreateOptionReq;
import com.superapp.branch_service.web.dto.ModifierDtos.UpdateGroupReq;
import com.superapp.branch_service.web.dto.ModifierDtos.UpdateOptionReq;
import com.superapp.branch_service.web.dto.PublishDto.PublishReq;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {
    private final MenuQueryService query;
    private final MenuWriteService write;
    private final PublishService publish;

    // READ
    @GetMapping("{branchId}")
    public Map<String, Object> getBranch(@PathVariable String branchId) {
        return query.getBranchMenu(branchId);
    }

    // CATEGORY
    @PostMapping("categories")
    public MenuCategory createCat(@Valid @RequestBody CreateCategoryReq req) {
        return write.createCategory(req);
    }

    @PatchMapping("categories/{id}")
    public MenuCategory updateCat(@PathVariable String id, @Valid @RequestBody UpdateCategoryReq req) {
        return write.updateCategory(id, req);
    }

    // DISH
    @GetMapping("{branchId}/dishes")
    public Paged<Dish> pageDishes(@PathVariable String branchId,
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String dir) {
        return query.pageDishes(branchId, categoryId, page, size, sort, dir);
    }

    @PostMapping("dishes")
    public Dish createDish(@Valid @RequestBody CreateDishReq req) {
        return write.createDish(req);
    }

    @PatchMapping("dishes/{id}")
    public Dish updateDish(@PathVariable String id, @Valid @RequestBody UpdateDishReq req) {
        return write.updateDish(id, req);
    }

    // MODIFIER
    @PostMapping("groups")
    public ModifierGroup createGroup(@Valid @RequestBody CreateGroupReq req) {
        return write.createGroup(req);
    }

    @PatchMapping("groups/{id}")
    public ModifierGroup updateGroup(@PathVariable String id, @Valid @RequestBody UpdateGroupReq req) {
        return write.updateGroup(id, req);
    }

    @PostMapping("options")
    public ModifierOption createOption(@Valid @RequestBody CreateOptionReq req) {
        return write.createOption(req);
    }

    @PatchMapping("options/{id}")
    public ModifierOption updateOption(@PathVariable String id, @Valid @RequestBody UpdateOptionReq req) {
        return write.updateOption(id, req);
    }

    // PUBLISH
    @PostMapping("publish")
    public Map<String, Object> publish(@Valid @RequestBody PublishReq req) {
        return publish.publish(req.branchId(), req.notes(), "system");
    }
}
