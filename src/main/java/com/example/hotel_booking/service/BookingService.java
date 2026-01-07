package com.example.hotel_booking.service;

import com.example.hotel_booking.model.dto.BookingRequestDto;
import com.example.hotel_booking.model.dto.BookingResponseDto;
import java.util.List;

public interface BookingService {

    List<BookingResponseDto> findAll();
    BookingResponseDto findById(Long id);
    BookingResponseDto create(
            Long roomId,
            String username,
            BookingRequestDto requestDto);
    void deleteById(Long id);

}
