package com.example.hotel_booking.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoomPageResponseDto {

    private List<RoomResponseDto> content;

    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}
