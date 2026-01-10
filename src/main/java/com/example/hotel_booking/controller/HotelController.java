package com.example.hotel_booking.controller;

import com.example.hotel_booking.model.dto.*;
import com.example.hotel_booking.service.HotelService;
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
@RequestMapping("/api/v1/hotels")
@Slf4j
public class HotelController {

    private final HotelService service;

    @GetMapping
    public ResponseEntity<HotelPageResponseDto> getAllHotels(
            @PageableDefault(size=10, sort = "name") Pageable pageable){

        log.info("Fetching hotels page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());

        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<HotelPageResponseDto> getHotelsByFilter(
            HotelFilterRequest filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(service.findHotels(filter, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelResponseDto> getHotelById(@PathVariable Long id){
        log.info("Fetching hotel with id={}", id);
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<HotelResponseDto> createHotel(@Valid @RequestBody HotelRequestDto request){
        log.info("Creating hotel: {}", request.getName());

        HotelResponseDto response = service.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HotelResponseDto> updateHotel(
            @PathVariable Long id,
            @Valid @RequestBody HotelRequestDto request){

        log.info("Updating hotel with id={}", id);

        return ResponseEntity.ok(service.update(id, request));
    }

    @PatchMapping("/{id}/rating")
    public ResponseEntity<HotelResponseDto> updateRaiting(
            @PathVariable Long id,
            @Valid @RequestBody HotelRatingRequestDto request
            ){
        return ResponseEntity.ok(service.updateRating(id, request.getNewMark()));



    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>  deleteHotel(@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
