package com.superapp.booking_service.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.superapp.booking_service.domain.Booking;
import com.superapp.booking_service.web.dto.BookingDtos.BookingRes;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface BookingMapper {
    /* ------------ Entity -> Response DTO ------------ */
    BookingRes toBookingRes(Booking booking);
}
