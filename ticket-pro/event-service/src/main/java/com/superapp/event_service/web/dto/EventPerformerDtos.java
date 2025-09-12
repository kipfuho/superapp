package com.superapp.event_service.web.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.UUID;

import com.superapp.event_service.domain.EventPerformer.Role;

public class EventPerformerDtos {
    // === Create ===
    public record CreateEventPerformerReq(
            @NotNull UUID eventId,
            @NotNull UUID performerId,
            @NotNull Role role,
            @NotNull Boolean headliner,
            Integer billingIndex, // 0 = top line; null if not ordered
            String stageName,
            LocalDateTime setStart,
            LocalDateTime setEnd) {
    }

    // === Update (partial) ===
    public record UpdateEventPerformerReq(
            @NotNull UUID id,
            Role role,
            Boolean headliner,
            Integer billingIndex,
            String stageName,
            LocalDateTime setStart,
            LocalDateTime setEnd) {
    }

    // === Response ===
    public record EventPerformerRes(
            UUID id,
            UUID eventId,
            UUID performerId,
            Role role,
            boolean headliner,
            Integer billingIndex,
            String stageName,
            LocalDateTime setStart,
            LocalDateTime setEnd,
            // Optional embedded summaries for convenience
            EventSummary event,
            PerformerDtos.PerformerSummary performer) {
        public record EventSummary(UUID id, String title, LocalDateTime startDateTime) {
        }
    }
}
