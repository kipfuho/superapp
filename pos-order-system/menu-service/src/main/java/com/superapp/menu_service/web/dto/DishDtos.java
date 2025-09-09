package com.superapp.menu_service.web.dto;

import java.util.List;

import com.superapp.menu_service.domain.ImageAsset;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class DishDtos {
    public record CreateDishReq(@NotBlank String branchId, @NotBlank String categoryId, @NotBlank String name,
            @NotBlank String sku, @Min(0) long basePrice, @NotBlank String taxRuleId, Boolean isActive,
            List<String> tagIds, Integer sort, List<ImageAsset> images) {
    }

    public record UpdateDishReq(String categoryId, String name, String sku, Long basePrice, String taxRuleId,
            Boolean isActive, List<String> tagIds, Integer sort, List<ImageAsset> images) {
    }
}
