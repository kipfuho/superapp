package com.superapp.menu_service.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;

public class CategoryDtos {
    public record CreateCategoryReq(@Null String id, @NotBlank String branchId, @NotBlank String name, @Min(0) int sort,
            Boolean active) {
    }

    public record UpdateCategoryReq(@NotBlank String id, @NotBlank String branchId, String name, Integer sort,
            Boolean active) {
    }
}
