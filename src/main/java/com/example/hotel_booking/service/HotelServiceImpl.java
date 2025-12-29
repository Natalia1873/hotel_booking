package com.example.hotel_booking.service;

import com.example.hotel_booking.exception.EntityNotFoundException;
import com.example.hotel_booking.mapper.HotelMapper;
import com.example.hotel_booking.model.dto.HotelPageResponseDto;
import com.example.hotel_booking.model.dto.HotelRequestDto;
import com.example.hotel_booking.model.dto.HotelResponseDto;
import com.example.hotel_booking.model.entity.Hotel;
import com.example.hotel_booking.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class HotelServiceImpl implements HotelService {

    private final HotelRepository repository;
    private final HotelMapper mapper;

    @Override
    public HotelPageResponseDto findAll(Pageable pageable) {
        Page<Hotel> page = repository.findAll(pageable);

        HotelPageResponseDto dto = new HotelPageResponseDto();
        dto.setContent(
                page.getContent().stream()
                        .map(mapper::toDto)
                        .toList()
        );
        dto.setPage(page.getNumber());
        dto.setSize(page.getSize());
        dto.setTotalElements(page.getTotalElements());

        log.info("Fetching hotels page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());

        return dto;
    }


    @Override
    public HotelResponseDto findById(Long id) {
        Hotel hotel = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Hotel with id=" + id + " not found"
                ));
        return mapper.toDto(hotel);
    }

    @Override
    public HotelResponseDto create(HotelRequestDto request) {

        log.info("Creating hotel: {}", request.getName());

        Hotel hotel = mapper.toEntity(request);

        hotel.setRating(0.0);
        hotel.setNumberOfRatings(0);

        Hotel saved = repository.save(hotel);

        return mapper.toDto(saved);
    }

    @Override
    public HotelResponseDto update(Long id, HotelRequestDto request) {

        log.info("Updating hotel with id={}", id);

        Hotel hotel = repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Hotel with id=" + id + " not found"));

        mapper.updateEntityFromDto(request, hotel);

        Hotel saved = repository.save(hotel);
        return mapper.toDto(saved);
    }

    @Override
    public void deleteById(Long id) {

        log.info("Deleting hotel with id={}", id);

        Hotel hotel = repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Hotel with id=" + id + " not found"));

        repository.delete(hotel);

    }
}
