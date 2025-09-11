package com.superapp.branch_service.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class ModifierDtos {
    public record CreateGroupReq(@NotBlank String branchId, @NotBlank String name, @Min(0) Integer min,
            @Min(0) Integer max,
            Boolean required, Integer sort) {
    }

    public record UpdateGroupReq(@NotBlank String id, String name, @Min(0) Integer min, @Min(0) Integer max,
            Boolean required, Integer sort, Boolean active) {
    }

    public record CreateOptionReq(@NotBlank String branchId, @NotBlank String groupId, @NotBlank String name,
            @Min(0) long priceDelta, Integer sort) {
    }

    public record UpdateOptionReq(@NotBlank String id, @NotBlank String groupId, String name,
            Long priceDelta,
            Boolean active, Integer sort) {
    }
}
