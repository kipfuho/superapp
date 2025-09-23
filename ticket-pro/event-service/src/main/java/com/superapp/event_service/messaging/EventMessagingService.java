package com.superapp.event_service.messaging;

import com.superapp.event_service.messaging.contract.EventCreated;
import java.util.concurrent.CompletableFuture;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EventMessagingService {

    private final KafkaTemplate<String, EventCreated> template;
    private final String topic;

    public EventMessagingService(
            KafkaTemplate<String, EventCreated> template,
            @Value("${app.topics.event-created}") String topic) {
        this.template = template;
        this.topic = topic;
    }

    public CompletableFuture<Void> publishEventCreated(EventCreated payload) {
        // Use eventId as the Kafka key to keep ordering per event
        ProducerRecord<String, EventCreated> record = new ProducerRecord<>(topic, payload.eventId(), payload);
        // add headers for routing/observability
        if (payload.traceId() != null) {
            record.headers().add(new RecordHeader("traceId", payload.traceId().getBytes()));
        }
        record.headers().add(new RecordHeader("eventType", "EventCreated".getBytes()));
        record.headers().add(new RecordHeader("source", "event-service".getBytes()));

        return template.send(record)
                .thenApply(result -> null);
    }
}
