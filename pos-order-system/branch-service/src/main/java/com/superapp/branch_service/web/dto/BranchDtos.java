package com.superapp.branch_service.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;

public class BranchDtos {
    public record CreateBranchReq(@Null String id, @NotBlank String name, @NotBlank String code, String brandId,
            Boolean active) {
    }

    public record UpdateBranchReq(@NotBlank String id, String name, String code, String brandId, Boolean active) {
    }
}
