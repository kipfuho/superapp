package com.superapp.event_service.messaging.contract;

public record EventCreated(
        String eventId,
        String venueId,
        String title,
        String description,
        String source, // "event-service"
        String traceId // for observability
) {
}
