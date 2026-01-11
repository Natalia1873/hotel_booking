package com.example.hotel_booking.statistics.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;

@Document(collection = "statistics")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatisticEventDocument {

    @Id
    private  String id;

    private String eventType;

    private Long userId;

    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    private Instant createdAt;
}
