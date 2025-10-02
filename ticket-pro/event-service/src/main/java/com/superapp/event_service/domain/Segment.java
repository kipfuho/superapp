package com.superapp.event_service.domain;

import java.time.Instant;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.CascadeType;

// import com.superapp.event_service.listener.SegmentListener;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
// import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "segments")
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE segments SET deleted_at = now(), version = version + 1 WHERE id = ? AND version = ?")
@SQLRestriction("deleted_at IS NULL") // replaces @Where
@Data
@NoArgsConstructor
@AllArgsConstructor
// @EntityListeners(SegmentListener.class)
public class Segment {
    public enum SegmentType {
        COMPOSITE, SECTION
    }

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private SegmentType segmentCategory;

    // link back to Venue
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Venue venue;

    // self-referencing parent for hierarchy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Segment parent;

    // children segments
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Segment> segments;

    private int totalPlaces;

    @Type(com.vladmihalcea.hibernate.type.json.JsonType.class)
    @Column(columnDefinition = "jsonb")
    private List<Place> places;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Place {
        private String id;
        private String label;
        private double x;
        private double y;
    }

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
