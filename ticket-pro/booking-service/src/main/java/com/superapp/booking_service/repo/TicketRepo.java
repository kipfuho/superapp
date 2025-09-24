package com.superapp.booking_service.repo;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.superapp.booking_service.domain.Ticket;

import io.lettuce.core.dynamic.annotation.Param;

public interface TicketRepo extends JpaRepository<Ticket, UUID> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
            INSERT INTO tickets (event_id, place_id, status, price, currency)
            VALUES (:eventId, :placeId, 'OPEN', :price, :currency)
            ON CONFLICT (event_id, place_id)
            DO UPDATE SET
              price     = EXCLUDED.price,
              currency  = EXCLUDED.currency
            WHERE tickets.status = 'OPEN'
               OR (tickets.status = 'RESERVED' AND tickets.reservation_expires_at IS NOT NULL AND tickets.reservation_expires_at < now())
            """, nativeQuery = true)
    int upsertOpen(@Param("eventId") UUID eventId,
            @Param("placeId") String placeId,
            @Param("price") BigDecimal price,
            @Param("currency") String currency);
}
