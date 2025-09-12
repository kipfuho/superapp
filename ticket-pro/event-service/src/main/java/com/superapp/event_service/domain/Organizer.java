package com.superapp.event_service.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "organizers")
@SQLDelete(sql = "UPDATE events SET deleted_at = now() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL") // replaces @Where
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Organizer {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;
    private String email;
    private String phone;
    private String website;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
