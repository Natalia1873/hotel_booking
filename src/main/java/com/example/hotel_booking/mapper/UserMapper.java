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
    @Mapping(target = "roles", expression = "java(Set.of(dto.getRole()))")
    User toEntity (UserRequestDto requestDto);

    void updateEntityFromDto(UserRequestDto requestDto,@MappingTarget User entity);

    @Mapping(target = "password", ignore = true)
    UserResponseDto toDto(User entity);
}
