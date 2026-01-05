package com.example.hotel_booking.mapper;

import com.example.hotel_booking.model.dto.BookingRequestDto;
import com.example.hotel_booking.model.dto.BookingResponseDto;
import com.example.hotel_booking.model.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookingMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "room", ignore = true)
    @Mapping(target = "user", ignore = true)
    Booking toEntity(BookingRequestDto dto);

    @Mapping(target = "room", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateEntityFromDto(BookingRequestDto dto, @MappingTarget Booking entity);

    @Mapping(source = "room.id", target = "roomId")
    @Mapping(source = "user.id", target = "userId")
    BookingResponseDto toDto(Booking entity);
}
