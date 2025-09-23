package com.superapp.event_service.service;

import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.superapp.event_service.domain.Venue;
import com.superapp.event_service.repo.VenueRepo;
import com.superapp.event_service.web.dto.VenueDtos.CreateVenueReq;
import com.superapp.event_service.web.dto.VenueDtos.UpdateVenueReq;
import com.superapp.event_service.web.mapper.VenueMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VenueService {
    private final VenueRepo repo;
    private final VenueMapper mapper;

    public Venue createVenue(CreateVenueReq req) {
        Venue v = mapper.toVenue(req);
        return repo.save(v);
    }

    public Venue updateVenue(UUID id, UpdateVenueReq req) {
        Venue v = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Venue not found"));
        mapper.updateVenue(req, v);
        return repo.save(v);
    }
}
