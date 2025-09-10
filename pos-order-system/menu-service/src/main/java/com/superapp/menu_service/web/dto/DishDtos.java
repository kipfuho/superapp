package com.superapp.menu_service.web.dto;

import java.util.List;

import com.superapp.menu_service.domain.ImageAsset;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;

public class DishDtos {
    public record CreateDishReq(@Null String id, @NotBlank String branchId, @NotBlank String categoryId,
            @NotBlank String name,
            @NotBlank String sku, @Min(0) long basePrice, @NotBlank String taxRuleId, Boolean active,
            List<String> tagIds, Integer sort, List<ImageAsset> images) {
    }

    public record UpdateDishReq(@NotBlank String id, @NotBlank String branchId, String categoryId, String name,
            String sku, Long basePrice,
            String taxRuleId,
            Boolean active, List<String> tagIds, Integer sort, List<ImageAsset> images) {
    }
}
