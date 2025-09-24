package com.superapp.event_service.repo;

import com.superapp.event_service.domain.Event;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepo extends JpaRepository<Event, UUID> {
    // all scheduled events at a venue
    List<Event> findByVenueIdAndStatus(UUID venueId, Event.EventStatus status);

    // with pagination
    Page<Event> findByVenueIdAndStatus(UUID venueId, Event.EventStatus status, Pageable pageable);

}