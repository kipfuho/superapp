package com.superapp.booking_service.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.superapp.booking_service.dao.TicketJdbcDao;
import com.superapp.booking_service.messaging.contract.TicketCreation;
import com.superapp.booking_service.messaging.contract.TicketCreation.PricingRule;
import com.superapp.booking_service.util.RandomUtils;
import com.superapp.booking_service.util.SeatUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketJdbcDao dao;

    public void createOrSyncTickets(TicketCreation payload) {
        List<String> placeIds = SeatUtils.expandGroupedPlaceIds(payload.groupedPlaceIds());
        PricingRule rule = payload.pricingRule();

        var eventIds = new ArrayList<UUID>();
        var placesFlat = new ArrayList<String>();
        var prices = new ArrayList<BigDecimal>();
        var currencies = new ArrayList<String>();

        for (String eventIdStr : payload.eventIds()) {
            UUID eventId = UUID.fromString(eventIdStr);
            for (String placeId : placeIds) {
                eventIds.add(eventId);
                placesFlat.add(placeId);
                prices.add(RandomUtils.randomBigDecimal(rule.minPrice(), rule.maxPrice(), 2));
                currencies.add(rule.currency());
            }
        }

        dao.upsertOpenBatch(eventIds, placeIds, prices, currencies);
    }
}
