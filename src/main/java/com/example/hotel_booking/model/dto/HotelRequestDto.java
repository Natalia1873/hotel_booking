package com.example.hotel_booking.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class HotelRequestDto {

    @NotBlank(message = "Name не может быть пустым")
    @Size(min=2, max=50, message = "Name должен быть от 2 до 50 символов")
    private String name;

    @Size(max=100, message = "Ad title не должен первышать 100 символов")
    private String adTitle;

    @NotBlank(message = "City не может быть пустым")
    private String city;

    @NotBlank(message = "Address не может быть пустым")
    private String address;

    @PositiveOrZero(message = "Distance не может быть отрицательной")
    private Double distanceFromCenter;

}
