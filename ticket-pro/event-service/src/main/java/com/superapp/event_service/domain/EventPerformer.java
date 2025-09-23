package com.superapp.event_service.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "event_performers", uniqueConstraints = @UniqueConstraint(name = "uk_event_performer", columnNames = {
        "event_id",
        "performer_id" }), indexes = @Index(name = "idx_event_billing", columnList = "event_id,billingIndex"))
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE event_performers SET deleted_at = now() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL") // replaces @Where
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventPerformer {

    public enum Role {
        HEADLINER, SUPPORT, SPECIAL_GUEST, OPENER, HOME, AWAY, CAST, SPEAKER
    }

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "performer_id")
    private Performer performer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private boolean headliner; // redundant with Role.HEADLINER but handy for queries
    private Integer billingIndex; // 0 = top line; null if N/A
    private String stageName; // “Main Stage”, “Arena A”
    private LocalDateTime setStart; // optional
    private LocalDateTime setEnd; // optional

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
