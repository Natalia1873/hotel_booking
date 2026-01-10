package com.example.hotel_booking.service;

import com.example.hotel_booking.exception.EntityNotFoundException;
import com.example.hotel_booking.mapper.HotelMapper;
import com.example.hotel_booking.model.dto.HotelFilterRequest;
import com.example.hotel_booking.model.dto.HotelPageResponseDto;
import com.example.hotel_booking.model.dto.HotelRequestDto;
import com.example.hotel_booking.model.dto.HotelResponseDto;
import com.example.hotel_booking.model.entity.Hotel;
import com.example.hotel_booking.repository.HotelRepository;
import com.example.hotel_booking.specification.HotelSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        dto.setTotalPages(page.getTotalPages());

        log.info("Fetching hotels page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());

        return dto;
    }

    @Override
    public HotelPageResponseDto findHotels(HotelFilterRequest filter, int page, int size) {

        Specification<Hotel> spec = Specification.where(null);

        spec = spec.and(HotelSpecification.hasId(filter.getId()))
                .and(HotelSpecification.hasName(filter.getName()))
                .and(HotelSpecification.hasAdTitle(filter.getAdTitle()))
                .and(HotelSpecification.hasCity(filter.getCity()))
                .and(HotelSpecification.hasAddress(filter.getAddress()))
                .and(HotelSpecification.hasDistanceFromCenter(filter.getDistanceFromCenter()))
                .and(HotelSpecification.hasRating(filter.getRating()))
                .and(HotelSpecification.hasNumberOfRatings(filter.getNumberOfRatings()));

        Pageable pageable = PageRequest.of(page, size);

        Page<Hotel> hotelPage = repository.findAll(spec, pageable);

        HotelPageResponseDto response  = new HotelPageResponseDto();
        response.setContent(
                hotelPage.getContent().stream()
                        .map(mapper::toDto)
                        .toList()
        );
        response.setPage(hotelPage.getNumber());
        response.setSize(hotelPage.getSize());
        response.setTotalElements(hotelPage.getTotalElements());
        response.setTotalPages(hotelPage.getTotalPages());

        return response;
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
    @Transactional
    public HotelResponseDto create(HotelRequestDto request) {

        log.info("Creating hotel: {}", request.getName());

        Hotel hotel = mapper.toEntity(request);

        hotel.setRating(0.0);
        hotel.setNumberOfRatings(0);

        Hotel saved = repository.save(hotel);

        return mapper.toDto(saved);
    }

    @Override
    @Transactional
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
    @Transactional
    public HotelResponseDto updateRating(Long hotelId, Integer newMark) {

        Hotel hotel = repository.findById(hotelId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Hotel with id=" + hotelId + " not found"
                ));
        double rating = hotel.getRating();
        int numberOfRating = hotel.getNumberOfRatings();

        double totalRating = rating * numberOfRating;
        totalRating = totalRating - rating + newMark;

        double newRating = totalRating / numberOfRating;
        newRating = Math.round(newRating * 10.0) / 10.0;

        hotel.setRating(newRating);
        hotel.setNumberOfRatings(numberOfRating + 1);
        return mapper.toDto(hotel);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {

        log.info("Deleting hotel with id={}", id);

        Hotel hotel = repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Hotel with id=" + id + " not found"));

        repository.delete(hotel);

    }
}
