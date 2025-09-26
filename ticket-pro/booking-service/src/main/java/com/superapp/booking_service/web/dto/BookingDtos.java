package com.superapp.booking_service.web.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public class BookingDtos {
    public record ReserveBookingReq(@NotNull List<UUID> ticketIds, @NotNull String customerId) {

    }

    public record GetBookingPaymentQrReq() {

    }

    // === Response ===
    public record BookingRes(UUID id, UUID eventId, List<String> placeIds, List<UUID> ticketIds, String status,
            BigDecimal totalAmount, String currency, String paymentMethod, Instant paymentAt) {

    }
}
