package com.superapp.branch_service.web.dto;

import java.util.List;

import com.superapp.branch_service.domain.Dish.AppliedGroup;
import com.superapp.branch_service.domain.ImageAsset;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class DishDtos {
    public record CreateDishReq(@NotBlank String branchId, @NotBlank String categoryId,
            @NotBlank String name,
            @NotBlank String sku, @Min(0) long basePrice, @NotBlank String taxRuleId,
            List<String> tagIds, Integer sort, List<ImageAsset> images, List<AppliedGroup> modifierGroups) {
    }

    public record UpdateDishReq(@NotBlank String id, String categoryId, String name,
            String sku, Long basePrice,
            String taxRuleId,
            Boolean active, List<String> tagIds, Integer sort, List<ImageAsset> images,
            List<AppliedGroup> modifierGroups) {
    }
}
