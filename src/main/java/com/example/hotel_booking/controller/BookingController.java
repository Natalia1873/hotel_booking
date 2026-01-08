package com.example.hotel_booking.controller;

import com.example.hotel_booking.model.dto.BookingRequestDto;
import com.example.hotel_booking.model.dto.BookingResponseDto;
import com.example.hotel_booking.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookings")
@Slf4j
public class BookingController {

    private final BookingService service;

    @GetMapping
    public ResponseEntity<List<BookingResponseDto>> getAllBookings(){
        log.info("Fetching all bookings");
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDto> getBookingById(
            @PathVariable Long id){
        log.info("Fetching booking with id={}", id);
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<BookingResponseDto> createBooking(
            @RequestParam Long roomId,
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody BookingRequestDto requestDto){


        log.info("Creating booking with roomId={} for user={}" , roomId, userDetails.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(roomId, userDetails.getUsername(),requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookingById(@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
