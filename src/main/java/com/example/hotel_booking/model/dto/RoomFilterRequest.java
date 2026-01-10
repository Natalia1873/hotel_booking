package com.example.hotel_booking.model.dto;

import com.example.hotel_booking.model.entity.Hotel;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class RoomFilterRequest {

    private Long id;
    private String name;

    private BigDecimal minPrice;
    private BigDecimal maxPrice;

    private Integer maxPeople;

    private LocalDate checkIn;
    private LocalDate checkOut;

    private Long hotelId;
}
