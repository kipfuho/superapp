package com.superapp.event_service.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
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
@SQLDelete(sql = "UPDATE events SET deleted_at = now() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL") // replaces @Where
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Event {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Lob
    private String description;

    private String category; // concert, sports, theater, etc.
    private String status; // scheduled, postponed, cancelled, sold_out

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

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
    private Double minPrice;
    private Double maxPrice;
    private String currency;
    private Integer ticketInventory;
    private LocalDateTime saleStart;
    private LocalDateTime saleEnd;

    // --- Media ---
    private String posterUrl;
    private String bannerUrl;

    // --- Extra metadata ---
    @ElementCollection
    private Set<String> tags;

    private String language;
    private String ageRestriction;
}
