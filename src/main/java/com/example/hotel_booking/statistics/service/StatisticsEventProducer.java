package com.example.hotel_booking.statistics.service;

import com.example.hotel_booking.statistics.event.RoomBookedEvent;
import com.example.hotel_booking.statistics.event.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class StatisticsEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendUserRegistered(Long userId){
        kafkaTemplate.send("user-registered", new UserRegisteredEvent(userId));
    }

    public void sendRoomBooked(Long userId, LocalDate in, LocalDate out) {
        kafkaTemplate.send("room-booked", new RoomBookedEvent(userId, in, out));
    }
}
