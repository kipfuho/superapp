package com.superapp.booking_service.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TicketJdbcDao {
    private final JdbcTemplate jdbc;

    public int upsertOpenBatch(List<UUID> eventIds,
            List<String> placeIds,
            List<BigDecimal> prices,
            List<String> currencies) {
        String sql = """
                  INSERT INTO tickets (id, event_id, place_id, status, price, currency, created_at, updated_at, version)
                  VALUES (?, ?, ?, 'OPEN', ?, ?, now(), now(), 0)
                  ON CONFLICT (event_id, place_id) DO UPDATE SET
                    price=EXCLUDED.price,
                    currency=EXCLUDED.currency
                WHERE tickets.status='OPEN'
                     OR (tickets.status='RESERVED'
                         AND tickets.reservation_expires_at IS NOT NULL
                         AND tickets.reservation_expires_at < now())
                """;

        int[] counts = jdbc.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(@NonNull PreparedStatement ps, int i) throws SQLException {
                ps.setObject(1, UUID.randomUUID());
                ps.setObject(2, eventIds.get(i));
                ps.setString(3, placeIds.get(i));
                ps.setBigDecimal(4, prices.get(i));
                ps.setString(5, currencies.get(i));
            }

            @Override
            public int getBatchSize() {
                return eventIds.size();
            }
        });

        return Arrays.stream(counts).sum();
    }

    public List<UUID> tryReserveAllAndReturnIds(List<UUID> ids, Instant until) {
        final String sql = """
                UPDATE tickets
                   SET status = 'RESERVED',
                       reservation_expires_at = ?
                 WHERE id = ANY (?)
                   AND (
                     status = 'OPEN'
                     OR (status = 'RESERVED' AND reservation_expires_at < now())
                   )
                 RETURNING id
                """;

        return jdbc.query(con -> {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setObject(1, until);
            ps.setArray(2, con.createArrayOf("uuid", ids.toArray()));
            return ps;
        }, (rs, rowNum) -> (UUID) rs.getObject("id"));
    }
}
