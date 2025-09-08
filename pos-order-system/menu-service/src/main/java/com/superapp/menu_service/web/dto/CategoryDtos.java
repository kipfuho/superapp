package com.superapp.menu_service.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class CategoryDtos {
    public record CreateCategoryReq(@NotBlank String branchId, @NotBlank String name, @Min(0) int sort,
            Boolean isActive) {
    }

    public record UpdateCategoryReq(String name, Integer sort, Boolean isActive) {
    }
}
