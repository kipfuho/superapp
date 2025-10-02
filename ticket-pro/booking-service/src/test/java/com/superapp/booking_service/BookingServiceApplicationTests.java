package com.superapp.booking_service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.superapp.booking_service.domain.Ticket;
import com.superapp.booking_service.domain.Booking.PaymentMethod;
import com.superapp.booking_service.messaging.TicketCreationConsumer;
import com.superapp.booking_service.messaging.contract.TicketCreation;
import com.superapp.booking_service.messaging.contract.TicketCreation.PricingRule;
import com.superapp.booking_service.repo.BookingRepo;
import com.superapp.booking_service.repo.TicketRepo;
import com.superapp.booking_service.service.TicketService;
import com.superapp.booking_service.web.dto.BookingDtos.BookingRes;
import com.superapp.booking_service.web.dto.BookingDtos.PayBookingReq;
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

    @Autowired
    BookingRepo bookingRepo;

    @Test
    void contextLoads() {
    }

    @BeforeEach
    void logStart(TestInfo testInfo) {
        System.out.println("\u001B[34m>>> Starting test: " + testInfo.getDisplayName() + "\u001B[0m");
    }

    @AfterEach
    void logEnd(TestInfo testInfo) {
        System.out.println("\u001B[32m<<< Finished test: " + testInfo.getDisplayName() + "\u001B[0m");
        // drop db
        bookingRepo.deleteAll();
        ticketRepo.deleteAll();
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
        mockMvc.perform(post("/booking").content(objectMapper.writeValueAsString(req))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    void bookingShouldNotSuccessAfterReserved() throws Exception {
        Ticket ticket = createTicket().get(0);
        var req = new ReserveBookingReq(List.of(ticket.getId()), "customerA");
        mockMvc.perform(post("/booking").content(objectMapper.writeValueAsString(req))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        var req2 = new ReserveBookingReq(List.of(ticket.getId()), "customerB");
        mockMvc.perform(post("/booking").content(objectMapper.writeValueAsString(req2))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isConflict());
    }

    @Test
    void getListBookingOfCustomer() throws Exception {
        Ticket ticket = createTicket().get(0);
        var req = new ReserveBookingReq(List.of(ticket.getId()), "customerA");
        mockMvc.perform(post("/booking").content(objectMapper.writeValueAsString(req))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        mockMvc.perform(get("/booking/customerA").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").isNotEmpty());
    }

    @Test
    void payBooking() throws Exception {
        Ticket ticket = createTicket().get(0);
        var req = new ReserveBookingReq(List.of(ticket.getId()), "customerA");
        mockMvc.perform(post("/booking").content(objectMapper.writeValueAsString(req))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        MvcResult result = mockMvc.perform(get("/booking/customerA").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").isNotEmpty()).andReturn();

        List<BookingRes> bookings = objectMapper.readValue(
                result.getResponse().getContentAsString(), new TypeReference<List<BookingRes>>() {
                });

        var req2 = new PayBookingReq(PaymentMethod.CUSTOM);
        mockMvc.perform(post("/booking/" + bookings.get(0).id() + "/payment-confirm")
                .content(objectMapper.writeValueAsString(req2)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("paymentMethod").value("CUSTOM"));
    }

    @Test
    void concurrentBooking() throws Exception {
        Ticket ticket = createTicket().get(0);
        UUID ticketId = ticket.getId();
        var reqA = new ReserveBookingReq(List.of(ticketId), "customerA");
        var reqB = new ReserveBookingReq(List.of(ticketId), "customerB");

        // Two threads block until we release them together
        var ready = new CountDownLatch(2);
        var start = new CountDownLatch(1);
        var pool = Executors.newFixedThreadPool(2);

        List<Future<MvcResult>> results = Stream.of(reqA, reqB)
                .map(req -> pool.submit(() -> {
                    ready.countDown();
                    start.await();
                    return mockMvc.perform(post("/booking")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(req)))
                            .andReturn();
                }))
                .toList();

        ready.await();
        start.countDown();
        MvcResult rA = results.get(0).get();
        MvcResult rB = results.get(1).get();
        pool.shutdown();

        // expect 1 succeed and 1 fail
        int sA = rA.getResponse().getStatus();
        int sB = rB.getResponse().getStatus();
        assertThat(List.of(sA, sB))
                .containsExactlyInAnyOrder(HttpStatus.OK.value(), HttpStatus.CONFLICT.value());
    }
}
