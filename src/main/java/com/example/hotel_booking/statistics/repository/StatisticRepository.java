package com.example.hotel_booking.statistics.repository;

import com.example.hotel_booking.statistics.model.StatisticEventDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StatisticRepository extends MongoRepository<StatisticEventDocument, String> {
}
