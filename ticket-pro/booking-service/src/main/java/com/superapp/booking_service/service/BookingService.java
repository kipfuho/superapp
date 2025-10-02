package com.superapp.booking_service.service;

import java.math.BigDecimal;
import java.time.Duration;
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
import com.superapp.booking_service.web.dto.BookingDtos.PayBookingReq;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepo repo;
    private final TicketRepo ticketRepo;
    private final TicketJdbcDao ticketDao;
    private final ReserveTokenService reserveTokenService;

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

        booking = repo.save(booking);
        String reserveToken = reserveTokenService.issue(booking.getId(), customerId, Duration.ofMinutes(9));
        booking.setReserveToken(reserveToken);
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
        booking.setReservationExpiresAt(reservationEndTime);

        booking = repo.save(booking);
        String reserveToken = reserveTokenService.issue(booking.getId(), customerId, Duration.ofMinutes(9));
        booking.setReserveToken(reserveToken);
        return repo.save(booking);
    }

    public void revokeBookingReserveToken(UUID bookingId, String token) {
        reserveTokenService.revokeByBookingId(bookingId, token);
    }

    public String getBookingPaymentQr(UUID bookingId, GetBookingPaymentQrReq req) {
        UUID reservedBookingId = reserveTokenService.resolveBookingId(req.reserveToken())
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired token"));

        if (bookingId != reservedBookingId) {
            throw new IllegalStateException("Token doesn't match booking");
        }

        Booking booking = repo.findById(bookingId)
                .orElseThrow(() -> new IllegalStateException("Booking not found"));

        if (booking.getReservationExpiresAt().isBefore(Instant.now())) {
            throw new IllegalStateException("Reservation expired");
        }
        // TODO: create qr gen factory for partner and return
        return "QRCODE";
    }

    public List<Booking> getBookingsByCustomer(String customerId) {
        return repo.findByCustomerId(customerId);
    }

    public Booking paymentBooking(UUID bookingId, PayBookingReq req) {
        Booking booking = repo.findById(bookingId)
                .orElseThrow(() -> new IllegalStateException("Booking not found"));

        booking.setPaymentMethod(req.paymentMethod());
        booking.setPaymentAt(Instant.now());
        return booking;
    }
}
