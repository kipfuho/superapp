package com.superapp.event_service.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.superapp.event_service.domain.Venue;
import com.superapp.event_service.service.VenueService;
import com.superapp.event_service.web.dto.VenueDtos.CreateVenueReq;
import com.superapp.event_service.web.dto.VenueDtos.UpdateVenueReq;

import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/venues")
@RequiredArgsConstructor
public class VenueController {
    private final VenueService venueService;

    @PostMapping("/")
    public Venue createVenue(@RequestBody CreateVenueReq req) {
        return venueService.createVenue(req);
    }

    @PostMapping("/{id}")
    public Venue updateVenue(@PathVariable UUID id, @RequestBody UpdateVenueReq req) {
        return venueService.updateVenue(id, req);
    }

}
