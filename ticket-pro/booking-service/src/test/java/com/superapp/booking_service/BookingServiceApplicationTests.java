package com.superapp.booking_service;

import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.superapp.booking_service.messaging.TicketCreationConsumer;
import com.superapp.booking_service.messaging.contract.TicketCreation;
import com.superapp.booking_service.messaging.contract.TicketCreation.PricingRule;
import com.superapp.booking_service.service.TicketService;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
class BookingServiceApplicationTests {

    @Container
    @ServiceConnection // Boot sets spring.data.mongodb.uri and waits for readiness
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:16-alpine"));

    @SuppressWarnings("resource")
    @org.testcontainers.junit.jupiter.Container
    @ServiceConnection
    static final GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:7-alpine"))
            .withExposedPorts(6379);

    @Autowired
    TicketCreationConsumer consumer;

    @MockitoBean
    TicketService ticketService;

    @Test
    void contextLoads() {
    }

    @Test
    void onTicketCreationEvent() {
        TicketCreation payload = new TicketCreation(List.of(UUID.randomUUID().toString()), UUID.randomUUID().toString(),
                "xyz",
                List.of("F1-[A,B]"), "event-service", "",
                new PricingRule(BigDecimal.valueOf(1), BigDecimal.valueOf(100), "VND"));
        var ack = org.mockito.Mockito.mock(Acknowledgment.class);
        consumer.onMessage(payload, "k-123", ack);
        verify(ticketService).createOrSyncTickets(payload);
    }
}
