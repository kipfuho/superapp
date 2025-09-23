package com.superapp.event_service.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.superapp.event_service.domain.Venue;
import com.superapp.event_service.web.dto.VenueDtos.CreateVenueReq;
import com.superapp.event_service.web.dto.VenueDtos.UpdateVenueReq;

@Mapper(componentModel = "spring")
public interface VenueMapper {
    Venue toVenue(CreateVenueReq req);

    void updateVenue(UpdateVenueReq req, @MappingTarget Venue target);
}
