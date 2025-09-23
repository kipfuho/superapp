package com.superapp.booking_service.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.superapp.booking_service.domain.Ticket;

public interface TicketRepo extends JpaRepository<Ticket, String> {

}
