package com.superapp.event_service.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "performers", indexes = {
        @Index(name = "idx_performer_name", columnList = "name"),
        @Index(name = "idx_performer_slug", columnList = "slug", unique = true)
})
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE performers SET deleted_at = now() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL") // replaces @Where
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Performer {

    public enum PerformerType {
        ARTIST, BAND, DJ, COMEDIAN, SPEAKER, THEATER_TROUPE, SPORTS_TEAM, OTHER
    }

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true, length = 120)
    private String slug; // SEO/id in URLs

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PerformerType type;

    @ElementCollection
    @CollectionTable(name = "performer_aliases", joinColumns = @JoinColumn(name = "performer_id"))
    @Column(name = "alias")
    private Set<String> aliases;

    @ElementCollection
    @CollectionTable(name = "performer_genres", joinColumns = @JoinColumn(name = "performer_id"))
    @Column(name = "genre")
    private Set<String> genres;

    private String countryCode; // ISO-3166-1 alpha-2
    private LocalDate formedOn; // for bands/teams
    private String imageUrl;
    private String bannerUrl;
    private String website;

    // Social links (key=value, e.g., "instagram" -> "https://...")
    @ElementCollection
    @CollectionTable(name = "performer_social", joinColumns = @JoinColumn(name = "performer_id"))
    @MapKeyColumn(name = "platform")
    @Column(name = "url")
    private java.util.Map<String, String> socialLinks;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
