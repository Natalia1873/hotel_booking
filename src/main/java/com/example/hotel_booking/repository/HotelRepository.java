package com.example.hotel_booking.repository;

import com.example.hotel_booking.model.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends JpaRepository <Hotel, Long> {
}
