package com.example.hotel_booking.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class HotelRatingRequestDto {

    @Min(1)
    @Max(5)
    private Integer newMark;
}
