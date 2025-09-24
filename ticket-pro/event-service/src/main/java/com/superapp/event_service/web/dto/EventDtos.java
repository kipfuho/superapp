package com.superapp.event_service.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.superapp.event_service.domain.EventPerformer;

public class EventDtos {

    // === Create ===
    public record CreateEventReq(
            @NotBlank @Size(max = 255) String title,
            String description,
            String category,
            String status,
            Instant startDateTime,
            Instant endDateTime,
            @PositiveOrZero BigDecimal minPrice,
            @PositiveOrZero BigDecimal maxPrice,
            @NotNull String currency,
            Integer ticketInventory,
            Instant saleStart,
            Instant saleEnd,
            String posterUrl,
            String bannerUrl,
            Set<String> tags,
            String language,
            String ageRestriction,
            @NotNull UUID venueId,
            @NotNull UUID organizerId,
            List<EventPerformer> lineup) {
    }

    // === Update ===
    public record UpdateEventReq(
            @NotNull UUID id,
            String title,
            String description,
            String category,
            String status,
            Instant startDateTime,
            Instant endDateTime,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String currency,
            Integer ticketInventory,
            Instant saleStart,
            Instant saleEnd,
            String posterUrl,
            String bannerUrl,
            Set<String> tags,
            String language,
            String ageRestriction,
            UUID venueId,
            UUID organizerId,
            List<EventPerformer> lineup) {
    }

    // === Response ===
    public record EventRes(
            UUID id,
            String title,
            String description,
            String category,
            String status,
            Instant startDateTime,
            Instant endDateTime,
            Instant createdAt,
            Instant updatedAt,
            UUID venueId,
            UUID organizerId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String currency,
            Integer ticketInventory,
            Instant saleStart,
            Instant saleEnd,
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
            Instant setStart,
            Instant setEnd) {
    }
}
