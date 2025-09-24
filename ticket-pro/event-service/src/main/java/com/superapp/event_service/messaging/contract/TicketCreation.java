package com.superapp.event_service.messaging.contract;

import java.math.BigDecimal;

public record TicketCreation(
        String eventId,
        String venueId,
        String title,
        String placeIds,
        String source, // "event-service"
        String traceId, // for observability
        PricingRule pricingRule) {
    public record PricingRule(
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String currency) {
    }
}
