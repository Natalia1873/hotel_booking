package com.example.hotel_booking.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class RoomRequestDto {

    @NotBlank(message = "Name не может быть пустым")
    @Size(min=2, max = 50, message = "Name должен быть от 2 до 50 символов")
    private String name;

    @NotBlank
    private String description;

    @NotNull
    @Positive
    private Integer number;

    @NotNull
    @Positive
    private BigDecimal price;

    @NotNull
    @Positive
    private Integer maxPeople;

    @FutureOrPresent
    private LocalDate occupiedFrom;

    @Future
    private LocalDate occupiedTo;

}
