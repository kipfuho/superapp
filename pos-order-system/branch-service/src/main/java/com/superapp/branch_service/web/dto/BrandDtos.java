package com.superapp.branch_service.web.dto;

import jakarta.validation.constraints.NotBlank;

public class BrandDtos {
    public record CreateBrandReq(@NotBlank String name, @NotBlank String code) {
    }

    public record UpdateBrandReq(@NotBlank String id, String name, Boolean active) {
    }
}
