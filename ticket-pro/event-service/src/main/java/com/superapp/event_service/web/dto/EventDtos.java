package com.superapp.event_service.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class EventDtos {

    // === Create ===
    public record CreateEventReq(
            @NotBlank @Size(max = 255) String title,
            String description,
            String category,
            String status,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            Double minPrice,
            Double maxPrice,
            String currency,
            Integer ticketInventory,
            LocalDateTime saleStart,
            LocalDateTime saleEnd,
            String posterUrl,
            String bannerUrl,
            Set<String> tags,
            String language,
            String ageRestriction,
            @NotNull UUID venueId,
            @NotNull UUID organizerId) {
    }

    // === Update ===
    public record UpdateEventReq(
            @NotNull UUID id,
            String title,
            String description,
            String category,
            String status,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            Double minPrice,
            Double maxPrice,
            String currency,
            Integer ticketInventory,
            LocalDateTime saleStart,
            LocalDateTime saleEnd,
            String posterUrl,
            String bannerUrl,
            Set<String> tags,
            String language,
            String ageRestriction,
            UUID venueId,
            UUID organizerId) {
    }

    // === Response ===
    public record EventRes(
            UUID id,
            String title,
            String description,
            String category,
            String status,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            UUID venueId,
            UUID organizerId,
            Double minPrice,
            Double maxPrice,
            String currency,
            Integer ticketInventory,
            LocalDateTime saleStart,
            LocalDateTime saleEnd,
            String posterUrl,
            String bannerUrl,
            Set<String> tags,
            String language,
            String ageRestriction,
            List<LineupItem> lineup) {
    }

    // === Nested DTO for lineup (performers) ===
    public record LineupItem(
            UUID performerId,
            String performerName,
            String role,
            boolean headliner,
            Integer billingIndex,
            String stageName,
            LocalDateTime setStart,
            LocalDateTime setEnd) {
    }
}
