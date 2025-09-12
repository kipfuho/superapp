package com.superapp.event_service.repo;

import com.superapp.event_service.domain.Organizer;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizerRepo extends JpaRepository<Organizer, UUID> {
}