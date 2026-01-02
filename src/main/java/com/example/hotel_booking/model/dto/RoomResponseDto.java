package com.example.hotel_booking.model.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class RoomResponseDto {

    private Long id;
    private String name;
    private String description;
    private Integer number;
    private BigDecimal price;
    private Integer maxPeople;
    private LocalDate occupiedFrom;
    private LocalDate occupiedTo;
    private Long hotelId;
}
