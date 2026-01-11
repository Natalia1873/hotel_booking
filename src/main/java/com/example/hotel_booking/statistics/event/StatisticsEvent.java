package com.example.hotel_booking.statistics.event;

import java.time.Instant;

public interface StatisticsEvent {
    String getEventType();
    Instant getCreatedAt();
}
