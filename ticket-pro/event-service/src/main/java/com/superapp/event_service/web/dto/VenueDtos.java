package com.superapp.event_service.web.dto;

import java.util.List;

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
}
