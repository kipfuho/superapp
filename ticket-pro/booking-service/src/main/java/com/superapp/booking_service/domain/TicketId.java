package com.superapp.booking_service.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketId implements Serializable {
    @Column(name = "event_id", nullable = false)
    private UUID eventId;

    @Column(name = "place_id", nullable = false, length = 128)
    private String placeId;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof TicketId that))
            return false;
        return Objects.equals(eventId, that.eventId) && Objects.equals(placeId, that.placeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, placeId);
    }
}
