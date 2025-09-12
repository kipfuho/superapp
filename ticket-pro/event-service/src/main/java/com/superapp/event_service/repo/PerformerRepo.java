package com.superapp.event_service.repo;

import com.superapp.event_service.domain.Performer;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PerformerRepo extends JpaRepository<Performer, UUID> {
}