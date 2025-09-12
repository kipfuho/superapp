package com.superapp.event_service.repo;

import com.superapp.event_service.domain.Event;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepo extends JpaRepository<Event, UUID> {
}