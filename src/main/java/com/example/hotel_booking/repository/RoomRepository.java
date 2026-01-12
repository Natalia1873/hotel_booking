package com.example.hotel_booking.repository;

import com.example.hotel_booking.model.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room,Long>,
        JpaSpecificationExecutor<Room> {
    Page<Room> findAllByHotelId(Long hotelId, Pageable pageable);

}
