package com.superapp.event_service.messaging;

import com.superapp.event_service.messaging.contract.TicketCreation;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EventMessagingService {

    private final KafkaTemplate<String, TicketCreation> template;
    private final String topic;

    public EventMessagingService(
            KafkaTemplate<String, TicketCreation> template,
            @Value("${app.topics.ticket-creation}") String topic) {
        this.template = template;
        this.topic = topic;
    }

    public CompletableFuture<Void> publishTicketCreation(TicketCreation payload) {
        ProducerRecord<String, TicketCreation> record = new ProducerRecord<>(topic, payload.eventIds().toString(),
                payload);

        if (payload.traceId() != null) {
            record.headers().add(new RecordHeader("traceId", payload.traceId().getBytes(StandardCharsets.UTF_8)));
        }
        record.headers().add(new RecordHeader("eventType", "TicketCreation".getBytes(StandardCharsets.UTF_8)));
        record.headers().add(new RecordHeader("source", "event-service".getBytes(StandardCharsets.UTF_8)));

        return template.send(record).thenApply(result -> null);
    }
}
