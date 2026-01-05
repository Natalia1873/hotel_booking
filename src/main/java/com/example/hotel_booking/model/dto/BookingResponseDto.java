package com.example.hotel_booking.model.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingResponseDto {

    private Long id;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Long roomId;
    private Long userId;
}
