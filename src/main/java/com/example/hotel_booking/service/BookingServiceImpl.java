package com.example.hotel_booking.service;

import com.example.hotel_booking.exception.EntityNotFoundException;
import com.example.hotel_booking.mapper.BookingMapper;
import com.example.hotel_booking.model.dto.BookingRequestDto;
import com.example.hotel_booking.model.dto.BookingResponseDto;
import com.example.hotel_booking.model.entity.Booking;
import com.example.hotel_booking.model.entity.Room;
import com.example.hotel_booking.model.entity.User;
import com.example.hotel_booking.repository.BookingRepository;
import com.example.hotel_booking.repository.RoomRepository;
import com.example.hotel_booking.repository.UserRepository;
import com.example.hotel_booking.statistics.service.StatisticsEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService{

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final BookingMapper mapper;
    private final StatisticsEventProducer statisticsEventProducer;

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDto> findAll() {
        log.info("Fetching all bookings");
        return bookingRepository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();

    }

    @Override
    public BookingResponseDto findById(Long id) {
        log.info("Fetching booking with id={}", id);
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Booking with id=" + id + " not found"
                ));
        return mapper.toDto(booking);
    }

    @Override
    @Transactional
    public BookingResponseDto create(Long roomId, String name, BookingRequestDto requestDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username= authentication.getName();

        log.info("Creating booking for roomId={}, username={}", roomId, username);

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Room with id=" + roomId + " not found"
                ));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User with username=" + username + " not found"
                ));

        boolean exists = bookingRepository.existsByRoomAndCheckInDateLessThanAndCheckOutDateGreaterThan(
                room,
                requestDto.getCheckOutDate(),
                requestDto.getCheckInDate()
        );

        if(exists) {
            throw  new IllegalArgumentException("Room is already booked for selected dates");
        }

        Booking booking = Booking.builder()
                .checkInDate(requestDto.getCheckInDate())
                .checkOutDate(requestDto.getCheckOutDate())
                .room(room)
                .user(user)
                .build();

        Booking saved = bookingRepository.save(booking);
        statisticsEventProducer.sendRoomBooked(
                user.getId(),
                saved.getCheckInDate(),
                saved.getCheckOutDate()
        );
        return mapper.toDto(saved);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Booking with id=" + id + " not found"
                ));
    bookingRepository.delete(booking);
    }
}
