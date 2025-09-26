package com.superapp.booking_service.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.superapp.booking_service.dao.TicketJdbcDao;
import com.superapp.booking_service.domain.Booking;
import com.superapp.booking_service.domain.Ticket;
import com.superapp.booking_service.repo.BookingRepo;
import com.superapp.booking_service.repo.TicketRepo;
import com.superapp.booking_service.web.dto.BookingDtos.GetBookingPaymentQrReq;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepo repo;
    private final TicketRepo ticketRepo;
    private final TicketJdbcDao ticketDao;

    @Transactional
    public Booking createBooking(UUID ticketId, String customerId) {
        // allow 10 minutes reservation for payment
        Instant reservationEndTime = Instant.now().plusSeconds(600);
        int updated = ticketRepo.tryReserve(ticketId, reservationEndTime);
        if (updated != 1) {
            throw new IllegalStateException("Ticket not bookable: " + ticketId);
        }

        Ticket ticket = ticketRepo.findById(ticketId)
                .orElseThrow(() -> new IllegalStateException("Ticket not found: " + ticketId));

        Booking booking = new Booking();
        booking.setTicketIds(List.of(ticket.getId()));
        booking.setEventId(ticket.getEventId());
        booking.setPlaceIds(List.of(ticket.getPlaceId()));
        booking.setCustomerId(customerId);
        booking.setTotalAmount(ticket.getPrice());
        booking.setCurrency(ticket.getCurrency());

        return repo.save(booking);
    }

    @Transactional
    public Booking createBooking(List<UUID> ticketIds, String customerId) {
        // allow 10 minutes reservation for payment
        Instant reservationEndTime = Instant.now().plusSeconds(600);
        List<UUID> reservedIds = ticketDao.tryReserveAllAndReturnIds(ticketIds, reservationEndTime);

        if (reservedIds.size() != ticketIds.size()) {
            List<UUID> failed = new ArrayList<>(ticketIds);
            failed.removeAll(reservedIds);

            throw new IllegalStateException("Failed to reserve: " + failed);
        }

        List<Ticket> tickets = ticketRepo.findAllById(ticketIds);
        UUID eventId = tickets.get(0).getEventId();
        String currency = tickets.get(0).getCurrency();
        boolean isSameEvent = tickets.stream().allMatch(t -> Objects.equals(eventId, t.getEventId()));
        if (!isSameEvent) {
            throw new IllegalStateException("All tickets in a booking must be for the same event");
        }

        List<String> placeIds = tickets.stream().map(Ticket::getPlaceId).toList();
        BigDecimal totalAmount = tickets.stream()
                .map(t -> t.getPrice() == null ? BigDecimal.ZERO : t.getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Booking booking = new Booking();
        booking.setTicketIds(ticketIds);
        booking.setEventId(eventId);
        booking.setPlaceIds(placeIds);
        booking.setCustomerId(customerId);
        booking.setTotalAmount(totalAmount);
        booking.setCurrency(currency);

        return repo.save(booking);
    }

    public String getBookingPaymentQr(UUID bookingId, GetBookingPaymentQrReq req) {
        return "QRCODE";
    }
}
