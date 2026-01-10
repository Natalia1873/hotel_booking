package com.example.hotel_booking.controller;

import com.example.hotel_booking.model.dto.*;
import com.example.hotel_booking.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hotels/{hotelId}/rooms")
@Slf4j

public class RoomController {

    private final RoomService service;

    @GetMapping
    public ResponseEntity<RoomPageResponseDto> getAllRooms(
            @PageableDefault(size = 10, sort = "name") Pageable pageable){

        log.info("Fetching rooms page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());

        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping("/search")
    public  ResponseEntity<RoomPageResponseDto> getRoomsByFilter(
            RoomFilterRequest filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(service.findByFilter(filter, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponseDto> getRoomById(@PathVariable Long id){

        log.info("Fetching room with id={}", id);

        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<RoomResponseDto> createRoom(
            @PathVariable Long hotelId,
            @Valid @RequestBody RoomRequestDto request){

        log.info("Creating room for hotelId={}", hotelId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(hotelId, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomResponseDto> updateRoomById(
            @PathVariable Long id,
            @Valid @RequestBody RoomRequestDto request
    ){
        log.info("Updating room with id={}", id);

        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoomById(@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
