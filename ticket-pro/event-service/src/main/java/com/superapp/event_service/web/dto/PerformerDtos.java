package com.superapp.event_service.web.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.superapp.event_service.domain.Performer.PerformerType;

public class PerformerDtos {
    // === Create ===
    public record CreatePerformerReq(
            @NotBlank @Size(max = 255) String name,
            @Size(max = 120) String slug, // optional; server may generate
            @NotNull PerformerType type,
            Set<@Size(max = 80) String> aliases,
            Set<@Size(max = 80) String> genres,
            @Pattern(regexp = "^[A-Z]{2}$", message = "ISO 3166-1 alpha-2") String countryCode,
            LocalDate formedOn,
            @Size(max = 2048) String imageUrl,
            @Size(max = 2048) String bannerUrl,
            @Size(max = 2048) String website,
            Map<@Size(max = 40) String, @Size(max = 2048) String> socialLinks) {
    }

    // === Update (partial) ===
    public record UpdatePerformerReq(
            @NotNull UUID id,
            String name,
            String slug,
            PerformerType type,
            Set<String> aliases,
            Set<String> genres,
            String countryCode,
            LocalDate formedOn,
            String imageUrl,
            String bannerUrl,
            String website,
            Map<String, String> socialLinks) {
    }

    // === Response ===
    public record PerformerRes(
            UUID id,
            String name,
            String slug,
            PerformerType type,
            Set<String> aliases,
            Set<String> genres,
            String countryCode,
            LocalDate formedOn,
            String imageUrl,
            String bannerUrl,
            String website,
            Map<String, String> socialLinks) {
    }

    // Lightweight summary for embeddings in other payloads
    public record PerformerSummary(
            UUID id,
            String name,
            String slug,
            PerformerType type,
            Set<String> genres,
            String imageUrl) {
    }
}
