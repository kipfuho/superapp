package com.superapp.booking_service.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
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
@Table(name = "tickets", uniqueConstraints = {
        @UniqueConstraint(name = "uq_tickets_event_place", columnNames = { "event_id", "place_id" })
})
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE tickets SET deleted_at = now() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {

    public enum TicketStatus {
        OPEN, RESERVED, BOOKED, CANCELLED, RESELLING
    }

    @Id
    @GeneratedValue
    private UUID id;

    @Column(insertable = false, updatable = false)
    private UUID eventId;

    @Column(insertable = false, updatable = false, length = 128)
    private String placeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    @Builder.Default
    private TicketStatus status = TicketStatus.OPEN;

    // Money
    @PositiveOrZero
    @Column(precision = 12, scale = 2)
    private BigDecimal price;

    @Column(length = 3)
    private String currency; // ISO 4217

    private Instant reservedAt;

    private Instant reservationExpiresAt;

    private Instant bookedAt;

    private Instant cancelledAt;

    private Instant resellingAt;

    private List<UUID> bookingId;

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
