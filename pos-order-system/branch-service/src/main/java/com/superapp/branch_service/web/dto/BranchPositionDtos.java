package com.superapp.branch_service.web.dto;

import jakarta.validation.constraints.*;
import java.util.Set;

public class BranchPositionDtos {
    public record CreatePositionReq(String branchId, @NotBlank String name, Set<String> permissions) {
    }

    public record UpdatePositionReq(@NotBlank String id, String name, Set<String> permissions, Boolean active) {
    }
}
