package com.superapp.event_service.web.dto;

import jakarta.validation.constraints.NotBlank;

public class VenueDtos {
    public record CreateVenueReq(@NotBlank String name, @NotBlank String code, String brandId) {
    }

    public record UpdateVenueReq(@NotBlank String id, String name, String brandId, Boolean active) {
    }
}
