package com.example.hotel_booking.service;

import com.example.hotel_booking.model.dto.*;
import com.example.hotel_booking.model.enums.RoleType;

import java.util.List;


public interface UserService {

    List<UserResponseDto> findAll();
    UserResponseDto findById(Long id);
    UserResponseDto findByUsername(String username);
    UserResponseDto create(UserRequestDto request, RoleType role);
    UserResponseDto update(Long id, UserRequestDto request);
    void deleteById(Long id);

}
