package com.superapp.event_service.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.superapp.event_service.domain.Event;
import com.superapp.event_service.domain.EventPerformer;
import com.superapp.event_service.web.dto.EventDtos.CreateEventReq;
import com.superapp.event_service.web.dto.EventDtos.EventRes;
import com.superapp.event_service.web.dto.EventDtos.LineupItem;
import com.superapp.event_service.web.dto.EventDtos.UpdateEventReq;

@Mapper(componentModel = "spring", uses = RefMapper.class)
public interface EventMapper {
    @Mapping(target = "venue", source = "venueId")
    @Mapping(target = "organizer", source = "organizerId")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    Event toEvent(CreateEventReq req);

    @Mapping(target = "venue", source = "venueId")
    @Mapping(target = "organizer", source = "organizerId")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateEvent(UpdateEventReq req, @MappingTarget Event target);

    /* ------------ Entity -> Response DTO ------------ */
    @Mapping(target = "venueId", source = "venue.id")
    @Mapping(target = "organizerId", source = "organizer.id")
    @Mapping(target = "lineup", source = "lineup") // MapStruct will use toLineupItem(...) for the list
    EventRes toEventRes(Event event);

    /* ------------ Nested: EventPerformer -> LineupItem ------------ */
    @Mapping(target = "performerId", source = "performer.id")
    @Mapping(target = "performerName", source = "performer.name")
    @Mapping(target = "role", source = "role")
    @Mapping(target = "headliner", source = "headliner")
    @Mapping(target = "billingIndex", source = "billingIndex")
    @Mapping(target = "stageName", source = "stageName")
    @Mapping(target = "setStart", source = "setStart")
    @Mapping(target = "setEnd", source = "setEnd")
    LineupItem toLineupItem(EventPerformer ep);
}
