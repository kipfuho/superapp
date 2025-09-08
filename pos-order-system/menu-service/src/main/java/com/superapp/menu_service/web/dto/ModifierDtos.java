package com.superapp.menu_service.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class ModifierDtos {
    public record CreateGroupReq(@NotBlank String branchId, @NotBlank String name, Integer min, Integer max,
            Boolean required, Integer sort) {
    }

    public record UpdateGroupReq(String name, Integer min, Integer max, Boolean required, Integer sort) {
    }

    public record CreateOptionReq(@NotBlank String groupId, @NotBlank String name, @Min(0) long priceDelta,
            Boolean isActive, Integer sort) {
    }

    public record UpdateOptionReq(String name, Long priceDelta, Boolean isActive, Integer sort) {
    }
}
