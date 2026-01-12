package com.example.service;

import com.example.hotel_booking.statistics.event.UserRegisteredEvent;
import com.example.hotel_booking.statistics.service.StatisticsEventProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserRegistrationKafkaTest {

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private StatisticsEventProducer eventProducer;

    @Test
    void sendUserRegistered_sendsCorrectEventToKafka() {
        Long userId = 100L;

        eventProducer.sendUserRegistered(userId);

        verify(kafkaTemplate).send(
                eq("user-registered"),
                argThat(argument -> {
                    if (!(argument instanceof UserRegisteredEvent event)) {
                        return false;
                    }
                    return event.getUserId().equals(userId);
                })
        );
    }
}

