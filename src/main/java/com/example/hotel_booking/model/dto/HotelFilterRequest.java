package com.example.hotel_booking.model.dto;

import lombok.Data;

@Data
public class HotelFilterRequest {

    private Long id;
    private String name;
    private String adTitle;
    private String city;
    private String address;
    private Double distanceFromCenter;
    private Double rating;
    private Integer numberOfRatings;
}
