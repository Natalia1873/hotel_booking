package com.example.hotel_booking.service;

import com.example.hotel_booking.model.dto.RoomPageResponseDto;
import com.example.hotel_booking.model.dto.RoomRequestDto;
import com.example.hotel_booking.model.dto.RoomResponseDto;
import org.springframework.data.domain.Pageable;


public interface RoomService {

    RoomPageResponseDto findAll(Pageable pageable);
    RoomResponseDto findById(Long id);
    RoomResponseDto create(Long hotelId, RoomRequestDto request);
    RoomResponseDto update(Long id, RoomRequestDto request);
    void deleteById(Long id);
}
