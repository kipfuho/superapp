package com.superapp.event_service.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.superapp.event_service.domain.Organizer;
import com.superapp.event_service.web.dto.OrganizerDtos.CreateOrganizerReq;
import com.superapp.event_service.web.dto.OrganizerDtos.UpdateOrganizerReq;

@Mapper(componentModel = "spring")
public interface OrganizerMapper {
    Organizer toOrganizer(CreateOrganizerReq req);

    void updateOrganizer(UpdateOrganizerReq req, @MappingTarget Organizer target);
}