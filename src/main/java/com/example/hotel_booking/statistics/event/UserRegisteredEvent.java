package com.example.hotel_booking.statistics.event;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
public class UserRegisteredEvent implements StatisticsEvent {

    private Long userId;
    private Instant createdAt = Instant.now();

    public UserRegisteredEvent(Long userId){
        this.userId = userId;
    }
    @Override
    public String getEventType() {
        return "USER_REGISTERED";
    }
}
