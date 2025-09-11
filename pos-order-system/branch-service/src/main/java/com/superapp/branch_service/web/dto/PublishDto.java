package com.superapp.branch_service.web.dto;

import jakarta.validation.constraints.NotBlank;

public class PublishDto {
    public record PublishReq(@NotBlank String branchId, String notes) {
    }
}
