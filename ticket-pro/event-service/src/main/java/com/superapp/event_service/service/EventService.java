package com.superapp.event_service.service;

import java.util.NoSuchElementException;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.superapp.event_service.domain.Event;
import com.superapp.event_service.messaging.EventMessagingService;
import com.superapp.event_service.messaging.contract.EventCreated;
import com.superapp.event_service.repo.EventRepo;
import com.superapp.event_service.web.dto.EventDtos.CreateEventReq;
import com.superapp.event_service.web.dto.EventDtos.EventRes;
import com.superapp.event_service.web.dto.EventDtos.UpdateEventReq;
import com.superapp.event_service.web.mapper.EventMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepo repo;
    private final EventMapper mapper;
    private final EventMessagingService messaging;

    @Transactional
    public EventRes createEvent(CreateEventReq req) {
        Event e = mapper.toEvent(req);
        Event event = repo.save(e);

        String traceId = MDC.get("traceId");
        var msg = new EventCreated(
                event.getId().toString(),
                event.getVenue().getId().toString(),
                event.getTitle(),
                event.getDescription(),
                "event-service",
                traceId);
        messaging.publishEventCreated(msg); // async

        return mapper.toEventRes(event);
    }

    public EventRes updateEvent(UUID id, UpdateEventReq req) {
        Event e = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Event not found"));
        mapper.updateEvent(req, e);
        Event event = repo.save(e);
        return mapper.toEventRes(event);
    }
}
