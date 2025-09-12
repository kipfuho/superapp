package com.superapp.event_service.service;

import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.superapp.event_service.domain.Event;
import com.superapp.event_service.repo.EventRepo;
import com.superapp.event_service.web.dto.EventDtos.CreateEventReq;
import com.superapp.event_service.web.dto.EventDtos.UpdateEventReq;
import com.superapp.event_service.web.mapper.EventMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventService {
    private EventRepo repo;
    private EventMapper mapper;

    public Event createEvent(CreateEventReq req) {
        Event e = mapper.toEvent(req);
        return repo.save(e);
    }

    public Event updateEvent(UUID id, UpdateEventReq req) {
        Event e = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Event not found"));
        mapper.updateEvent(req, e);
        return repo.save(e);
    }
}
