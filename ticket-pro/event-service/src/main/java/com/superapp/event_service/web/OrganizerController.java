package com.superapp.event_service.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.superapp.event_service.domain.Organizer;
import com.superapp.event_service.service.OrganizerService;
import com.superapp.event_service.web.dto.OrganizerDtos.CreateOrganizerReq;
import com.superapp.event_service.web.dto.OrganizerDtos.UpdateOrganizerReq;

import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/organizers")
@RequiredArgsConstructor
public class OrganizerController {
    private final OrganizerService organizerService;

    @PostMapping("/")
    public Organizer createOrganizer(@RequestBody CreateOrganizerReq req) {
        return organizerService.createOrganizer(req);
    }

    @PostMapping("/{id}")
    public Organizer updateOrganizer(@PathVariable UUID id, @RequestBody UpdateOrganizerReq req) {
        return organizerService.updateOrganizer(id, req);
    }

}
