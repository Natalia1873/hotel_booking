package com.example.service;

import com.example.hotel_booking.exception.EntityAlreadyExistsException;
import com.example.hotel_booking.mapper.UserMapper;
import com.example.hotel_booking.model.dto.UserRequestDto;
import com.example.hotel_booking.model.dto.UserResponseDto;
import com.example.hotel_booking.model.entity.User;
import com.example.hotel_booking.model.enums.RoleType;
import com.example.hotel_booking.repository.UserRepository;
import com.example.hotel_booking.service.UserServiceImpl;
import com.example.hotel_booking.statistics.service.StatisticsEventProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper mapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private StatisticsEventProducer statisticsEventProducer;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void create_success() {
        UserRequestDto request = new UserRequestDto();
        request.setUsername("user1");
        request.setEmail("user1@mail.com");
        request.setPassword("password");

        RoleType role = RoleType.ROLE_USER;

        User userEntity = new User();
        userEntity.setId(1L);
        userEntity.setUsername("user1");
        userEntity.setEmail("user1@mail.com");

        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(1L);
        responseDto.setUsername("user1");
        responseDto.setEmail("user1@mail.com");

        when(repository.existsByUsername("user1")).thenReturn(false);
        when(repository.existsByEmail("user1@mail.com")).thenReturn(false);
        when(mapper.toEntity(request)).thenReturn(userEntity);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(repository.save(any(User.class))).thenReturn(userEntity);
        when(mapper.toDto(userEntity)).thenReturn(responseDto);

        UserResponseDto result = userService.create(request, role);

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("user1");
        assertThat(result.getEmail()).isEqualTo("user1@mail.com");

        verify(passwordEncoder).encode("password");
        verify(repository).save(any(User.class));
        verify(statisticsEventProducer).sendUserRegistered(1L);
    }

    @Test
    void create_usernameAlreadyExists_shouldThrowException() {
        UserRequestDto request = new UserRequestDto();
        request.setUsername("user1");
        request.setEmail("user1@mail.com");

        when(repository.existsByUsername("user1")).thenReturn(true);

        assertThatThrownBy(() ->
                userService.create(request, RoleType.ROLE_USER)
        ).isInstanceOf(EntityAlreadyExistsException.class);

        verify(repository, never()).save(any());
        verifyNoInteractions(statisticsEventProducer);
    }

    @Test
    void create_emailAlreadyExists_shouldThrowException() {
        UserRequestDto request = new UserRequestDto();
        request.setUsername("user1");
        request.setEmail("user1@mail.com");

        when(repository.existsByUsername("user1")).thenReturn(false);
        when(repository.existsByEmail("user1@mail.com")).thenReturn(true);

        assertThatThrownBy(() ->
                userService.create(request, RoleType.ROLE_USER)
        ).isInstanceOf(EntityAlreadyExistsException.class);

        verify(repository, never()).save(any());
        verifyNoInteractions(statisticsEventProducer);
    }
}
