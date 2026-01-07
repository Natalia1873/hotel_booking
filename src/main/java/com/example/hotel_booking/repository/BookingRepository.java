package com.example.hotel_booking.repository;

import com.example.hotel_booking.model.entity.Booking;
import com.example.hotel_booking.model.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    boolean existsByRoomAndCheckInDateLessThanAndCheckOutDateGreaterThan(
            Room room,
            LocalDate checkOutDate,
            LocalDate checkInDate
    );

}
