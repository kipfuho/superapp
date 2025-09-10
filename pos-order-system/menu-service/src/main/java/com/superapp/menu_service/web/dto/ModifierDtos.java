package com.superapp.menu_service.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;

public class ModifierDtos {
    public record CreateGroupReq(@Null String id, @NotBlank String branchId, @NotBlank String name, Integer min,
            Integer max,
            Boolean required, Integer sort) {
    }

    public record UpdateGroupReq(@NotBlank String id, @NotBlank String branchId, String name, Integer min, Integer max,
            Boolean required, Integer sort) {
    }

    public record CreateOptionReq(@Null String id, @NotBlank String groupId, @NotBlank String name,
            @Min(0) long priceDelta,
            Boolean active, Integer sort) {
    }

    public record UpdateOptionReq(@NotBlank String id, @NotBlank String branchId, @NotBlank String groupId, String name,
            Long priceDelta,
            Boolean active, Integer sort) {
    }
}
