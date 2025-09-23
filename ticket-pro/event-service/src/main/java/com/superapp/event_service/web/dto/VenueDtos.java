package com.superapp.event_service.web.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.superapp.event_service.domain.Segment;

import jakarta.validation.constraints.NotBlank;

public class VenueDtos {
    public record CreateVenueReq(@NotBlank String name, String address, String city, String country, Integer capacity,
            List<Segment> segments) {
    }

    public record UpdateVenueReq(@NotBlank String id, @NotBlank String name, String address, String city,
            String country,
            Integer capacity,
            List<Segment> segments) {
    }

    // === Response ===
    public record VenueRes(
            UUID id,
            String name,
            String address,
            String city,
            String country,
            Integer capacity,
            List<SegmentRes> segments,
            Instant createdAt,
            Instant updatedAt) {
    }

    public record SegmentRes(
            String id,
            String name,
            Segment.SegmentType segmentCategory,
            Integer totalPlaces,
            List<PlaceRes> places,
            List<SegmentRes> segments, // children only; no venue back-ref
            String parentId // optional: handy for UI; not a back-ref object
    ) {
    }

    public record PlaceRes(
            String id,
            String label,
            double x,
            double y) {
    }
}
