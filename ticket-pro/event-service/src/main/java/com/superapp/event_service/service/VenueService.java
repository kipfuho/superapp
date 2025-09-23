package com.superapp.event_service.service;

import java.util.NoSuchElementException;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import com.superapp.event_service.messaging.EventMessagingService;
import com.superapp.event_service.domain.Venue;
import com.superapp.event_service.messaging.contract.TicketCreation;
import com.superapp.event_service.repo.VenueRepo;
import com.superapp.event_service.util.PlaceUtils;
import com.superapp.event_service.util.SeatGrouper;
import com.superapp.event_service.util.SegmentIdBuilder;
import com.superapp.event_service.web.dto.VenueDtos.CreateVenueReq;
import com.superapp.event_service.web.dto.VenueDtos.UpdateVenueReq;
import com.superapp.event_service.web.dto.VenueDtos.VenueRes;
import com.superapp.event_service.web.mapper.VenueMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VenueService {
    private final VenueRepo repo;
    private final VenueMapper mapper;
    private final EventMessagingService messaging;

    public VenueRes getVenue(UUID id) {
        Venue venue = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Venue not found"));
        return mapper.toVenueRes(venue);
    }

    public VenueRes createVenue(CreateVenueReq req) {
        Venue v = mapper.toVenue(req);
        SegmentIdBuilder.ensureSegmentIds(v);
        Venue venue = repo.save(v);
        return mapper.toVenueRes(venue);
    }

    public VenueRes updateVenue(UUID id, UpdateVenueReq req) {
        Venue v = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Venue not found"));
        mapper.updateVenue(req, v);
        SegmentIdBuilder.ensureSegmentIds(v);
        Venue venue = repo.save(v);

        var placeIds = PlaceUtils.collectPlaceIdsFromSegments(venue.getSegments());
        var grouped = SeatGrouper.groupPlaceIds(placeIds);
        var spec = String.join(",", grouped);

        String traceId = MDC.get("traceId");
        var msg = new TicketCreation(
                "ALL",
                venue.getId().toString(),
                "Venue Updated",
                spec,
                "event-service",
                traceId);
        messaging.publishTicketCreation(msg); // async

        return mapper.toVenueRes(venue);
    }
}
