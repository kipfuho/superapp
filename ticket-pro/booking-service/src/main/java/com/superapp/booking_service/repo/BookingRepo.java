package com.superapp.booking_service.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.superapp.booking_service.domain.Booking;

public interface BookingRepo extends JpaRepository<Booking, UUID> {

}
