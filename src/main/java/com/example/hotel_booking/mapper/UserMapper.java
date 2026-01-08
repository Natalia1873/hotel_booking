package com.example.hotel_booking.mapper;

import com.example.hotel_booking.model.dto.UserRequestDto;
import com.example.hotel_booking.model.dto.UserResponseDto;
import com.example.hotel_booking.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toEntity (UserRequestDto requestDto);

    void updateEntityFromDto(UserRequestDto requestDto,@MappingTarget User entity);

    UserResponseDto toDto(User entity);
}
