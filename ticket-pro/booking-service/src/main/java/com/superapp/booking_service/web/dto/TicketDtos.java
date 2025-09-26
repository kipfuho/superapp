package com.superapp.booking_service.web.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class TicketDtos {
    // === Response ===
    public record TicketRes(UUID id, UUID eventId, String placeId, String status, BigDecimal price, String currency) {

    }
}
