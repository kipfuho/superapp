package com.superapp.event_service.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.superapp.event_service.service.EventService;
import com.superapp.event_service.web.dto.EventDtos.CreateEventReq;
import com.superapp.event_service.web.dto.EventDtos.EventRes;
import com.superapp.event_service.web.dto.EventDtos.UpdateEventReq;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping("/")
    public EventRes createEvent(@Valid @RequestBody CreateEventReq req) {
        return eventService.createEvent(req);
    }

    @PostMapping("/{id}")
    public EventRes updateEvent(@PathVariable UUID id, @Valid @RequestBody UpdateEventReq req) {
        return eventService.updateEvent(id, req);
    }
}
