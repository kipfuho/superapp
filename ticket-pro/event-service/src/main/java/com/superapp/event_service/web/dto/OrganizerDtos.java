package com.superapp.event_service.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

public class OrganizerDtos {

    // === Create ===
    public record CreateOrganizerReq(
            @NotBlank @Size(max = 255) String name,
            @Email String email,
            String phone,
            String website,
            String description) {
    }

    // === Update ===
    public record UpdateOrganizerReq(
            UUID id, // required to know which organizer
            String name,
            @Email String email,
            String phone,
            String website,
            String description) {
    }

    // === Response ===
    public record OrganizerRes(
            UUID id,
            String name,
            String email,
            String phone,
            String website,
            String description,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
    }
}
