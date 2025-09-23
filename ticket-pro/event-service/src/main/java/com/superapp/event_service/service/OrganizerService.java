package com.superapp.event_service.service;

import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.superapp.event_service.domain.Organizer;
import com.superapp.event_service.repo.OrganizerRepo;
import com.superapp.event_service.web.dto.OrganizerDtos.CreateOrganizerReq;
import com.superapp.event_service.web.dto.OrganizerDtos.UpdateOrganizerReq;
import com.superapp.event_service.web.mapper.OrganizerMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrganizerService {
    private final OrganizerRepo repo;
    private final OrganizerMapper mapper;

    public Organizer createOrganizer(CreateOrganizerReq req) {
        Organizer v = mapper.toOrganizer(req);
        return repo.save(v);
    }

    public Organizer updateOrganizer(UUID id, UpdateOrganizerReq req) {
        Organizer v = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Organizer not found"));
        mapper.updateOrganizer(req, v);
        return repo.save(v);
    }
}
