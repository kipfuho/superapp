package com.superapp.branch_service.web.dto;

import com.superapp.branch_service.domain.DiningTable;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class TableDtos {
    public record CreateAreaReq(@NotBlank String branchId, @NotBlank String name, Integer sort) {
    }

    public record UpdateAreaReq(@NotBlank String id, String name, Integer sort,
            Boolean active) {
    }

    public record CreateTableReq(@NotBlank String branchId, @NotBlank String code, String name,
            @Min(1) Integer capacity, String areaId, Integer sort) {
    }

    public record UpdateTableReq(@NotBlank String id, String name, Integer capacity, String areaId,
            Integer sort, DiningTable.TableStatus status, Boolean active, String mergedIntoId) {
    }
}
