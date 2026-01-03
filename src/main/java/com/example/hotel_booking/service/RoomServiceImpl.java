package com.example.hotel_booking.service;

import com.example.hotel_booking.exception.EntityNotFoundException;
import com.example.hotel_booking.mapper.RoomMapper;
import com.example.hotel_booking.model.dto.RoomPageResponseDto;
import com.example.hotel_booking.model.dto.RoomRequestDto;
import com.example.hotel_booking.model.dto.RoomResponseDto;
import com.example.hotel_booking.model.entity.Hotel;
import com.example.hotel_booking.model.entity.Room;
import com.example.hotel_booking.repository.HotelRepository;
import com.example.hotel_booking.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final RoomMapper mapper;

    @Override
    public RoomPageResponseDto findAll(Pageable pageable) {
        log.info("Fetching rooms page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());

        Page<Room> page = roomRepository.findAll(pageable);

        RoomPageResponseDto dto = new RoomPageResponseDto();
        dto.setContent(
                page.getContent().stream()
                        .map(mapper::toDto)
                        .toList()
        );
        dto.setPage(page.getNumber());
        dto.setSize(page.getSize());
        dto.setTotalPages(page.getTotalPages());
        dto.setTotalElements(page.getTotalElements());

        return dto;
    }

    @Override
    public RoomResponseDto findById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Room with id=" + id + " not found"
                ));
        return mapper.toDto(room);
    }

    @Override
    @Transactional
    public RoomResponseDto create(Long hotelId, RoomRequestDto request) {

        log.info("Creating room: {}", request.getName());

        Hotel hotel= hotelRepository.findById(hotelId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Hotel with id=" + hotelId + " not found"
                ));

        Room room = mapper.toEntity(request);
        room.setHotel(hotel);
        Room saved = roomRepository.save(room);
        return mapper.toDto(saved);
    }

    @Override
    @Transactional
    public RoomResponseDto update(Long id, RoomRequestDto request) {

        log.info("Updating room with id={}", id);

        Room room = roomRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(
                        "Room with id=" + id + " not found"
                ));
        mapper.updateEntityFromDto(request, room);
        Room saved = roomRepository.save(room);
        return mapper.toDto(saved);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {

        log.info("Deleting room with id={}", id);

        if(!roomRepository.existsById(id)){
            throw new EntityNotFoundException(
                    "Room with id=" + id + " not found"
            );
        }
        roomRepository.deleteById(id);

    }
}
