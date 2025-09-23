package com.superapp.event_service.messaging.contract;

public record TicketCreation(
        String eventId,
        String venueId,
        String title,
        String placeIds,
        String source, // "event-service"
        String traceId // for observability
) {
}
