package com.superapp.event_service.web.mapper;

import org.springframework.stereotype.Component;

import com.superapp.event_service.domain.Organizer;
import com.superapp.event_service.domain.Venue;

import jakarta.persistence.EntityManager;

@Component
public class RefMapper {
    @jakarta.persistence.PersistenceContext
    private EntityManager em;

    public Venue toVenue(java.util.UUID id) {
        return id == null ? null : em.getReference(Venue.class, id);
    }

    public Organizer toOrganizer(java.util.UUID id) {
        return id == null ? null : em.getReference(Organizer.class, id);
    }
}