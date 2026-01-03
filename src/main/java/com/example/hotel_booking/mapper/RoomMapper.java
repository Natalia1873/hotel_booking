package com.example.hotel_booking.mapper;

import com.example.hotel_booking.model.dto.RoomRequestDto;
import com.example.hotel_booking.model.dto.RoomResponseDto;
import com.example.hotel_booking.model.entity.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoomMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hotel", ignore = true)
    Room toEntity(RoomRequestDto dto);

    @Mapping(target = "hotel", ignore = true)
    void updateEntityFromDto(RoomRequestDto dto,@MappingTarget Room entity);

    RoomResponseDto toDto(Room entity);


}
