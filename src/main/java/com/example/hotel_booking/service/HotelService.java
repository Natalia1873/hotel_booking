package com.example.hotel_booking.service;

import com.example.hotel_booking.model.dto.HotelPageResponseDto;
import com.example.hotel_booking.model.dto.HotelRequestDto;
import com.example.hotel_booking.model.dto.HotelResponseDto;
import org.springframework.data.domain.Pageable;

public interface HotelService {

    HotelPageResponseDto findAll(Pageable pageable);
    HotelResponseDto findById(Long id);
    HotelResponseDto create(HotelRequestDto request);
    HotelResponseDto update(Long id, HotelRequestDto request);
    HotelResponseDto updateRating(Long hotelId, Integer newMark);
    void deleteById(Long id);
}
