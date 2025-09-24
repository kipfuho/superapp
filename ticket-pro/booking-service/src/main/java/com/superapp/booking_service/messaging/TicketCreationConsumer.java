package com.superapp.booking_service.messaging;

import com.superapp.booking_service.messaging.contract.TicketCreation;
import com.superapp.booking_service.service.TicketService; // your domain logic
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Component;

@Component
public class TicketCreationConsumer {

    private final TicketService ticketService;

    public TicketCreationConsumer(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @KafkaListener(topics = "${app.topics.ticket-creation}", containerFactory = "ticketCreationKafkaListenerContainerFactory")
    public void onMessage(
            TicketCreation payload,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            ConsumerRecord<String, TicketCreation> record,
            Acknowledgment ack) {
        try {
            ticketService.createOrSyncTickets(payload);
            ack.acknowledge();
        } catch (Exception e) {
            // rethrow to trigger retry/DLT (don't ack)
            throw e;
        }
    }
}
