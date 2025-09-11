package com.superapp.branch_service.web.dto;

import jakarta.validation.constraints.NotBlank;

public class BranchDtos {
    public record CreateBranchReq(@NotBlank String name, @NotBlank String code, String brandId) {
    }

    public record UpdateBranchReq(@NotBlank String id, String name, String brandId, Boolean active) {
    }
}
