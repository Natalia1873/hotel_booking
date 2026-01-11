package com.example.hotel_booking.statistics.service;

import com.example.hotel_booking.statistics.event.RoomBookedEvent;
import com.example.hotel_booking.statistics.event.UserRegisteredEvent;
import com.example.hotel_booking.statistics.model.StatisticEventDocument;
import com.example.hotel_booking.statistics.repository.StatisticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticsEventListener {

    private final StatisticRepository repository;

    @KafkaListener(topics = "user-registered")
    public  void handleUserRegistered(UserRegisteredEvent event){
        repository.save(
                StatisticEventDocument.builder()
                        .eventType(event.getEventType())
                        .userId(event.getUserId())
                        .createdAt(event.getCreatedAt())
                        .build()
        );
    }

    @KafkaListener(topics = "room-booked")
    public void handleRoomBooked(RoomBookedEvent event) {
        repository.save(
                StatisticEventDocument.builder()
                        .eventType(event.getEventType())
                        .userId(event.getUserId())
                        .checkInDate(event.getCheckInDate())
                        .checkOutDate(event.getCheckOutDate())
                        .createdAt(event.getCreatedAt())
                        .build()
        );
    }
}
