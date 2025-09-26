package com.superapp.booking_service.web;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.superapp.booking_service.domain.Booking;
import com.superapp.booking_service.service.BookingService;
import com.superapp.booking_service.web.dto.BookingDtos.BookingRes;
import com.superapp.booking_service.web.dto.BookingDtos.GetBookingPaymentQrReq;
import com.superapp.booking_service.web.dto.BookingDtos.ReserveBookingReq;
import com.superapp.booking_service.web.mapper.BookingMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private final BookingMapper bookingMapper;

    @PostMapping("/{eventId}")
    public BookingRes booking(@PathVariable UUID eventId, @RequestBody ReserveBookingReq req) {
        if (req.ticketIds().size() == 1) {
            Booking b = bookingService.createBooking(req.ticketIds().get(0), req.customerId());
            return bookingMapper.toBookingRes(b);
        }

        Booking b = bookingService.createBooking(req.ticketIds(), req.customerId());
        return bookingMapper.toBookingRes(b);
    }

    @PostMapping("/{bookingId}/qr")
    public String getBookingPaymentQr(@PathVariable UUID bookingId, @RequestBody GetBookingPaymentQrReq req) {
        return bookingService.getBookingPaymentQr(bookingId, req);
    }
}
