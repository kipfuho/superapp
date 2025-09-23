package com.superapp.event_service.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.superapp.event_service.service.VenueService;
import com.superapp.event_service.web.dto.VenueDtos.CreateVenueReq;
import com.superapp.event_service.web.dto.VenueDtos.UpdateVenueReq;
import com.superapp.event_service.web.dto.VenueDtos.VenueRes;

import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/venues")
@RequiredArgsConstructor
public class VenueController {
    private final VenueService venueService;

    @PostMapping("/")
    public VenueRes createVenue(@RequestBody CreateVenueReq req) {
        return venueService.createVenue(req);
    }

    @GetMapping("/{id}")
    public VenueRes updateVenue(@PathVariable UUID id) {
        return venueService.getVenue(id);
    }

    @PostMapping("/{id}")
    public VenueRes updateVenue(@PathVariable UUID id, @RequestBody UpdateVenueReq req) {
        return venueService.updateVenue(id, req);
    }

}
