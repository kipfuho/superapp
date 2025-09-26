package com.superapp.booking_service.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.superapp.booking_service.domain.Booking;
import com.superapp.booking_service.domain.Booking.BookingStatus;
import com.superapp.booking_service.repo.BookingRepo;
import com.superapp.booking_service.web.dto.WebHookDtos.PaymentCallBackReq;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WebHookService {
    private final BookingRepo bookingRepo;
    private final PartnerAuthService partnerAuthService;

    public void paymentBooking(UUID bookingId, String partnerId, PaymentCallBackReq req) {
        partnerAuthService.validatePartnerRequest(partnerId, req.toMap(), req.checksum());
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new IllegalStateException("Booking not found: " + bookingId));
        if (booking.getStatus() == BookingStatus.COMPLETED || booking.getStatus() == BookingStatus.CANCELLED) {
            throw new IllegalStateException("Booking completed or cancelled: " + bookingId);
        }

        if (booking.getTotalAmount().compareTo(req.amount()) != 0) {
            throw new IllegalStateException("Amount not match: " + bookingId);
        }

        booking.setStatus(Booking.BookingStatus.COMPLETED);
        booking.setPaymentMethod(partnerId);
        booking.setPaymentAt(Instant.now());
        bookingRepo.save(booking);
    }
}
