package com.superapp.booking_service.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.superapp.booking_service.messaging.contract.TicketCreation;
import com.superapp.booking_service.messaging.contract.TicketCreation.PricingRule;
import com.superapp.booking_service.repo.TicketRepo;
import com.superapp.booking_service.util.RandomUtils;
import com.superapp.booking_service.util.SeatUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepo repo;

    public void createOrSyncTickets(TicketCreation payload) {
        List<String> placeIds = SeatUtils.expandGroupedPlaceIds(payload.groupedPlaceIds());
        PricingRule rule = payload.pricingRule();
        for (String eventId : payload.eventIds()) {
            for (String placeId : placeIds) {
                BigDecimal price = RandomUtils.randomBigDecimal(rule.minPrice(), rule.maxPrice(), 2);
                repo.upsertOpen(UUID.fromString(eventId), placeId, price, rule.currency());
            }
        }
    }
}
