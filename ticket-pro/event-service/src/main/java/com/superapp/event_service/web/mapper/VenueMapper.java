package com.superapp.event_service.web.mapper;

import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.superapp.event_service.domain.Segment;
import com.superapp.event_service.domain.Venue;
import com.superapp.event_service.web.dto.VenueDtos.CreateVenueReq;
import com.superapp.event_service.web.dto.VenueDtos.PlaceRes;
import com.superapp.event_service.web.dto.VenueDtos.SegmentRes;
import com.superapp.event_service.web.dto.VenueDtos.UpdateVenueReq;
import com.superapp.event_service.web.dto.VenueDtos.VenueRes;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface VenueMapper {
    @Mapping(target = "segments", source = "segments")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    Venue toVenue(CreateVenueReq req);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateVenue(UpdateVenueReq req, @MappingTarget Venue target);

    /* -------- After-mapping: wire backrefs -------- */
    @AfterMapping
    default void wireBackrefs(@MappingTarget Venue venue) {
        if (venue.getSegments() != null) {
            for (Segment s : venue.getSegments()) {
                linkSegmentTree(venue, null, s);
            }
        }
    }

    private void linkSegmentTree(Venue venue, Segment parent, Segment current) {
        current.setVenue(venue);
        current.setParent(parent);
        if (current.getSegments() != null) {
            for (Segment child : current.getSegments()) {
                linkSegmentTree(venue, current, child);
            }
        }
    }

    // --- Entity -> Response DTO (no venue back-ref) ---
    @Mapping(target = "segments", source = "segments")
    VenueRes toVenueRes(Venue venue);

    @Mapping(target = "parentId", source = "parent.id") // id only, no parent object
    SegmentRes toSegmentRes(Segment s);

    List<SegmentRes> toSegmentResList(List<Segment> s);

    // Places (domain -> DTO)
    PlaceRes toPlaceRes(Segment.Place p);

    List<PlaceRes> toPlaceResList(List<Segment.Place> p);
}
