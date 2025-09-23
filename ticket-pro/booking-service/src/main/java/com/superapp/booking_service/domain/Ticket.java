package com.superapp.booking_service.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "tickets", indexes = {
        @Index(name = "idx_ticket_event_status", columnList = "event_id,status"),
        @Index(name = "idx_ticket_customer_status", columnList = "customer_id,status")
})
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE tickets SET deleted_at = now() WHERE event_id = ? AND place_id = ?")
@SQLRestriction("deleted_at IS NULL")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {

    public enum TicketStatus {
        OPEN, RESERVED, BOOKED, CANCELLED, RESELLING
    }

    @EmbeddedId
    private TicketId id;

    @Column(name = "event_id", insertable = false, updatable = false)
    private java.util.UUID eventId;

    @Column(name = "place_id", insertable = false, updatable = false, length = 128)
    private String placeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private TicketStatus status;

    // Money
    @PositiveOrZero
    @Column(precision = 12, scale = 2)
    private BigDecimal price;

    @Pattern(regexp = "^[A-Z]{3}$") // ISO 4217
    @Column(length = 3)
    private String currency;

    @Column(name = "reserved_at")
    private Instant reservedAt;

    @Column(name = "booked_at")
    private Instant bookedAt;

    @Column(name = "cancelled_at")
    private Instant cancelledAt;

    @Column(name = "reselling_at")
    private Instant resellingAt;

    @Column(name = "reservation_expires_at")
    private Instant reservationExpiresAt;

    @Column(name = "hold_token", length = 64)
    private String holdToken;

    @Column(name = "customer_id")
    private UUID customerId;

    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "payment_id")
    private String paymentId;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @Version
    private long version;
}
