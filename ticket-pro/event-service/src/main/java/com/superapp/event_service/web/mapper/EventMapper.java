package com.superapp.event_service.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.superapp.event_service.domain.Event;
import com.superapp.event_service.web.dto.EventDtos.CreateEventReq;
import com.superapp.event_service.web.dto.EventDtos.UpdateEventReq;

@Mapper(componentModel = "spring")
public interface EventMapper {
    Event toEvent(CreateEventReq req);

    void updateEvent(UpdateEventReq req, @MappingTarget Event target);
}
