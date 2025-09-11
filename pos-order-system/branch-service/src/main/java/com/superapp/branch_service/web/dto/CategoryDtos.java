package com.superapp.branch_service.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class CategoryDtos {
    public record CreateCategoryReq(@NotBlank String branchId, @NotBlank String name, @Min(0) int sort) {
    }

    public record UpdateCategoryReq(@NotBlank String id, String name, Integer sort,
            Boolean active) {
    }
}
