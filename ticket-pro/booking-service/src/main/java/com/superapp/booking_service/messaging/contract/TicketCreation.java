package com.superapp.booking_service.messaging.contract;

import java.math.BigDecimal;
import java.util.List;

public record TicketCreation(
        List<String> eventIds,
        String venueId,
        String title,
        List<String> groupedPlaceIds,
        String source, // "event-service"
        String traceId, // for observability
        PricingRule pricingRule) {
    public record PricingRule(
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String currency) {
    }
}
