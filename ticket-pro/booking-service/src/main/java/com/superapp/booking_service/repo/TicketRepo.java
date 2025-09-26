package com.superapp.booking_service.repo;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.superapp.booking_service.domain.Ticket;

import io.lettuce.core.dynamic.annotation.Param;

public interface TicketRepo extends JpaRepository<Ticket, UUID> {

    @Transactional
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

    @Transactional
    @Modifying
    @Query("""
                update Ticket t
                   set t.status = 'RESERVED',
                       t.reservationExpiresAt = :time
                 where t.id = :id
                   and (
                     t.status = 'OPEN'
                     or (t.status = 'RESERVED' and t.reservationExpiresAt < CURRENT_TIMESTAMP )
                   )
            """)
    int tryReserve(@Param("id") UUID id, @Param("time") Instant reservationTime);

    @Transactional
    @Modifying
    @Query("""
                update Ticket t
                   set t.status = 'RESERVED',
                       t.reservationExpiresAt = :time
                 where t.id in :ids
                   and (
                     t.status = 'OPEN'
                     or (t.status = 'RESERVED' and t.reservationExpiresAt < CURRENT_TIMESTAMP)
                   )
            """)
    int tryReserveAll(@Param("ids") List<UUID> ids, @Param("time") Instant reservationTime);

    Optional<Ticket> findByIdAndEventId(UUID ticketId, UUID eventId);
}
