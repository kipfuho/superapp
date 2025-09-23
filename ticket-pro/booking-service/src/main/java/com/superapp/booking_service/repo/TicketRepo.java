package com.superapp.booking_service.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.superapp.booking_service.domain.Ticket;

public interface TicketRepo extends JpaRepository<Ticket, UUID> {

}
