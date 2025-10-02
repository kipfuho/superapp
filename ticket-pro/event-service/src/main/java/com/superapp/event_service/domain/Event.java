package com.superapp.event_service.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "events")
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE events SET deleted_at = now(), version = version + 1 WHERE id = ? AND version = ?")
@SQLRestriction("deleted_at IS NULL") // replaces @Where
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Event {

    public enum EventStatus {
        SCHEDULED, POSTPONED, CANCELLED, COMPLETED
    }

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Lob
    private String description;

    private String category; // concert, sports, theater, etc.

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    @Builder.Default
    private EventStatus status = EventStatus.SCHEDULED;

    private Instant startDateTime;

    private Instant endDateTime;

    // --- Venue relationship ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id")
    private Venue venue;

    // --- Organizer relationship ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizer_id")
    private Organizer organizer;

    // --- Performer relationship ---
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("billingIndex ASC NULLS LAST")
    private java.util.List<EventPerformer> lineup;

    // --- Ticketing ---
    @PositiveOrZero
    @Column(precision = 12, scale = 2)
    private BigDecimal minPrice;
    @PositiveOrZero
    @Column(precision = 12, scale = 2)
    private BigDecimal maxPrice;

    @Pattern(regexp = "^[A-Z]{3}$") // ISO 4217
    @Column(length = 3)
    private String currency;

    private Integer ticketInventory;

    private Instant saleStart;

    private Instant saleEnd;

    // --- Media ---
    private String posterUrl;

    private String bannerUrl;

    // --- Extra metadata ---
    @ElementCollection
    private Set<String> tags;

    private String language;

    private String ageRestriction;

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
