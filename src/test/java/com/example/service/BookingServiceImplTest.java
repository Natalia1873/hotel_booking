package com.example.service;

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
import com.example.hotel_booking.service.BookingServiceImpl;
import com.example.hotel_booking.statistics.service.StatisticsEventProducer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingMapper bookingMapper;

    @Mock
    private StatisticsEventProducer statisticsEventProducer;

    @InjectMocks
    private BookingServiceImpl bookingService;


    @BeforeEach
    void setUpSecurityContext() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("user1");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void createBooking_success() {
        // given
        Long roomId = 1L;
        String username = "user1";

        BookingRequestDto request = new BookingRequestDto();
        request.setCheckInDate(LocalDate.now().plusDays(1));
        request.setCheckOutDate(LocalDate.now().plusDays(3));

        Room room = Room.builder().id(roomId).build();
        User user = User.builder().id(10L).username(username).build();

        Booking savedBooking = Booking.builder()
                .id(100L)
                .room(room)
                .user(user)
                .checkInDate(request.getCheckInDate())
                .checkOutDate(request.getCheckOutDate())
                .build();

        BookingResponseDto responseDto = new BookingResponseDto();
        responseDto.setId(100L);

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(bookingRepository.existsByRoomAndCheckInDateLessThanAndCheckOutDateGreaterThan(
                any(), any(), any())).thenReturn(false);
        when(bookingRepository.save(any())).thenReturn(savedBooking);
        when(bookingMapper.toDto(savedBooking)).thenReturn(responseDto);

        // when
        BookingResponseDto result = bookingService.create(roomId, username, request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(100L);
    }

    @Test
    void createBooking_roomAlreadyBooked_shouldThrowException() {
        // given
        Long roomId = 1L;
        String username = "user1";

        BookingRequestDto request = new BookingRequestDto();
        request.setCheckInDate(LocalDate.now().plusDays(1));
        request.setCheckOutDate(LocalDate.now().plusDays(3));

        Room room = Room.builder().id(roomId).build();
        User user = User.builder().username(username).build();

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(bookingRepository.existsByRoomAndCheckInDateLessThanAndCheckOutDateGreaterThan(
                any(), any(), any())).thenReturn(true);

        // then
        assertThatThrownBy(() ->
                bookingService.create(roomId, username, request)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Room is already booked");
    }

    @Test
    void createBooking_roomNotFound_shouldThrowException() {
        Long roomId = 1L;

        BookingRequestDto request = new BookingRequestDto();
        request.setCheckInDate(LocalDate.now().plusDays(1));
        request.setCheckOutDate(LocalDate.now().plusDays(3));

        when(roomRepository.findById(roomId)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                bookingService.create(roomId, "user1", request)
        )
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Room with id=" + roomId);

        verifyNoInteractions(userRepository);
        verifyNoInteractions(bookingRepository);
        verifyNoInteractions(statisticsEventProducer);
    }

    @Test
    void createBooking_userNotFound_shouldThrowException() {
        Long roomId = 1L;

        BookingRequestDto request = new BookingRequestDto();
        request.setCheckInDate(LocalDate.now().plusDays(1));
        request.setCheckOutDate(LocalDate.now().plusDays(3));

        Room room = Room.builder().id(roomId).build();

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(userRepository.findByUsername("user1")).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                bookingService.create(roomId, "user1", request)
        )
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("User with username=user1");

        verify(bookingRepository, never()).save(any());
        verifyNoInteractions(statisticsEventProducer);
    }

    @Test
    void createBooking_whenDatesOverlap_shouldNotSaveAndNotSendEvent() {
        Long roomId = 1L;

        BookingRequestDto request = new BookingRequestDto();
        request.setCheckInDate(LocalDate.now().plusDays(1));
        request.setCheckOutDate(LocalDate.now().plusDays(3));

        Room room = Room.builder().id(roomId).build();
        User user = User.builder().id(10L).username("user1").build();

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user));
        when(bookingRepository.existsByRoomAndCheckInDateLessThanAndCheckOutDateGreaterThan(
                any(), any(), any())
        ).thenReturn(true);

        assertThatThrownBy(() ->
                bookingService.create(roomId, "user1", request)
        )
                .isInstanceOf(IllegalArgumentException.class);

        verify(bookingRepository, never()).save(any());
        verifyNoInteractions(statisticsEventProducer);
    }








}
