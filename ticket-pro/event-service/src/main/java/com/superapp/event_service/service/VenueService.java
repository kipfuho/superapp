package com.superapp.event_service.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import com.superapp.event_service.messaging.EventMessagingService;
import com.superapp.event_service.domain.Event;
import com.superapp.event_service.domain.Venue;
import com.superapp.event_service.domain.Event.EventStatus;
import com.superapp.event_service.messaging.contract.TicketCreation;
import com.superapp.event_service.repo.EventRepo;
import com.superapp.event_service.repo.VenueRepo;
import com.superapp.event_service.util.SeatUtils;
import com.superapp.event_service.util.SegmentBuilder;
import com.superapp.event_service.web.dto.VenueDtos.CreateVenueReq;
import com.superapp.event_service.web.dto.VenueDtos.UpdateVenueReq;
import com.superapp.event_service.web.dto.VenueDtos.VenueRes;
import com.superapp.event_service.web.mapper.VenueMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VenueService {
    private final EventRepo eventRepo;
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
        SegmentBuilder.ensureSegments(v);
        Venue venue = repo.save(v);
        return mapper.toVenueRes(venue);
    }

    public VenueRes updateVenue(UUID id, UpdateVenueReq req) {
        Venue v = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Venue not found"));
        mapper.updateVenue(req, v);
        SegmentBuilder.ensureSegments(v);
        Venue venue = repo.save(v);

        List<Event> events = eventRepo.findByVenueIdAndStatus(venue.getId(), EventStatus.SCHEDULED);
        List<String> groupedPlaceIds = SeatUtils.groupPlaceIds(venue.getSegments());
        String traceId = MDC.get("traceId");
        TicketCreation msg = new TicketCreation(
                events.stream().map(e -> e.getId().toString()).toList(),
                venue.getId().toString(),
                "Venue Updated",
                groupedPlaceIds,
                "event-service",
                traceId,
                null);
        messaging.publishTicketCreation(msg); // async

        return mapper.toVenueRes(venue);
    }
}
