package com.superapp.event_service.repo;

import com.superapp.event_service.domain.Venue;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VenueRepo extends JpaRepository<Venue, UUID> {
}