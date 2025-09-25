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

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bookings")
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE bookings SET deleted_at = now() WHERE event_id = ? AND place_id = ?")
@SQLRestriction("deleted_at IS NULL")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    public enum BookingStatus {
        INPROGRESS, COMPLETED, CANCELLED
    }

    @Id
    @GeneratedValue
    private UUID id;

    private UUID eventId;

    private List<String> placeIds;

    private List<UUID> ticketIds;

    private String customerId;

    @Column(precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @Column(length = 3) // ISO 4217
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    @Builder.Default
    private BookingStatus status = BookingStatus.INPROGRESS;

    private String paymentMethod;

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
