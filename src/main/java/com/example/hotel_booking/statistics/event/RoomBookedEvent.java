package com.example.hotel_booking.statistics.event;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class RoomBookedEvent implements StatisticsEvent {
    private Long userId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Instant createdAt = Instant.now();


    public RoomBookedEvent(
            Long userId,
            LocalDate checkInDate,
            LocalDate checkOutDate
    ){
        this.userId = userId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    @Override
    public String getEventType() {
        return "ROOM_BOOKED";
    }
}
