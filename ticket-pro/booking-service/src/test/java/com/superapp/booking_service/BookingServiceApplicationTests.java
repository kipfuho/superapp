package com.superapp.booking_service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.superapp.booking_service.domain.Ticket;
import com.superapp.booking_service.messaging.TicketCreationConsumer;
import com.superapp.booking_service.messaging.contract.TicketCreation;
import com.superapp.booking_service.messaging.contract.TicketCreation.PricingRule;
import com.superapp.booking_service.repo.TicketRepo;
import com.superapp.booking_service.service.TicketService;
import com.superapp.booking_service.web.dto.BookingDtos.ReserveBookingReq;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
@AutoConfigureMockMvc
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
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    TicketCreationConsumer consumer;

    @Autowired
    TicketService ticketService;

    @Autowired
    TicketRepo ticketRepo;

    @Test
    void contextLoads() {
    }

    List<Ticket> createTicket() {
        UUID eventId = UUID.randomUUID();
        TicketCreation payload = new TicketCreation(List.of(eventId.toString()), UUID.randomUUID().toString(),
                "xyz",
                List.of("F1-[A,B]"), "event-service", "",
                new PricingRule(BigDecimal.valueOf(1), BigDecimal.valueOf(100), "VND"));
        var ack = org.mockito.Mockito.mock(Acknowledgment.class);
        consumer.onMessage(payload, "k-123", ack);
        var tickets = ticketRepo.findByEventId(eventId);
        return tickets;
    }

    @Test
    void onTicketCreationEvent() {
        var tickets = createTicket();
        assertThat(tickets).hasSize(2);
    }

    @Test
    void bookingSuccess() throws Exception {
        Ticket ticket = createTicket().get(0);
        var req = new ReserveBookingReq(List.of(ticket.getId()), "customerA");
        mockMvc.perform(post("/booking/").content(objectMapper.writeValueAsString(req))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    void bookingShouldNotSuccessAfterReserved() throws Exception {
        Ticket ticket = createTicket().get(0);
        var req = new ReserveBookingReq(List.of(ticket.getId()), "customerA");
        mockMvc.perform(post("/booking/").content(objectMapper.writeValueAsString(req))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        var req2 = new ReserveBookingReq(List.of(ticket.getId()), "customerB");
        mockMvc.perform(post("/booking/").content(objectMapper.writeValueAsString(req2))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().is5xxServerError());
    }

    @Test
    void getListBookingOfCustomer() throws Exception {
        Ticket ticket = createTicket().get(0);
        var req = new ReserveBookingReq(List.of(ticket.getId()), "customerA");
        mockMvc.perform(post("/booking/").content(objectMapper.writeValueAsString(req))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        mockMvc.perform(get("/booking/customerA").contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].id").isNotEmpty());
    }
}
