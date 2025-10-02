package com.superapp.booking_service.web;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.superapp.booking_service.domain.Booking;
import com.superapp.booking_service.service.BookingService;
import com.superapp.booking_service.web.dto.BookingDtos.BookingRes;
import com.superapp.booking_service.web.dto.BookingDtos.GetBookingPaymentQrReq;
import com.superapp.booking_service.web.dto.BookingDtos.PayBookingReq;
import com.superapp.booking_service.web.dto.BookingDtos.ReserveBookingReq;
import com.superapp.booking_service.web.mapper.BookingMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private final BookingMapper bookingMapper;

    @GetMapping("/{customerId}")
    public List<BookingRes> getBookingsByCustomer(@PathVariable String customerId) {
        List<Booking> bookings = bookingService.getBookingsByCustomer(customerId);
        return bookings.stream().map(bookingMapper::toBookingRes).toList();
    }

    @PostMapping("")
    public BookingRes booking(@RequestBody ReserveBookingReq req) {
        if (req.ticketIds().size() == 1) {
            Booking b = bookingService.createBooking(req.ticketIds().get(0), req.customerId());
            return bookingMapper.toBookingRes(b);
        }

        Booking b = bookingService.createBooking(req.ticketIds(), req.customerId());
        return bookingMapper.toBookingRes(b);
    }

    @GetMapping("/{bookingId}/revoke")
    public String getBookingPaymentQr(@PathVariable UUID bookingId, @RequestParam("reserveToken") String token) {
        bookingService.revokeBookingReserveToken(bookingId, token);
        return "ok";
    }

    @PostMapping("/{bookingId}/qr")
    public String getBookingPaymentQr(@PathVariable UUID bookingId, @RequestBody GetBookingPaymentQrReq req) {
        return bookingService.getBookingPaymentQr(bookingId, req);
    }

    @PostMapping("/{bookingId}/payment-confirm")
    public BookingRes payBooking(@PathVariable UUID bookingId, @RequestBody PayBookingReq req) {
        Booking b = bookingService.paymentBooking(bookingId, req);
        return bookingMapper.toBookingRes(b);
    }
}
