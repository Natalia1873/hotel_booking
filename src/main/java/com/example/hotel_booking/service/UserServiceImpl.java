package com.example.hotel_booking.service;

import com.example.hotel_booking.exception.EntityAlreadyExistsException;
import com.example.hotel_booking.exception.EntityNotFoundException;
import com.example.hotel_booking.mapper.UserMapper;
import com.example.hotel_booking.model.dto.UserRequestDto;
import com.example.hotel_booking.model.dto.UserResponseDto;
import com.example.hotel_booking.model.entity.User;
import com.example.hotel_booking.model.enums.RoleType;
import com.example.hotel_booking.repository.UserRepository;
import com.example.hotel_booking.statistics.service.StatisticsEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final StatisticsEventProducer statisticsEventProducer;

    @Override
    public List<UserResponseDto> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public UserResponseDto findById(Long id) {

        log.info("Fetching user with id={}", id);

        User user = repository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(
                        "User with id=" + id + " not found"
                ));
        return mapper.toDto(user);
    }

    @Override
    public UserResponseDto findByUsername(String username) {

        log.info("Fetching user with username={}", username);

        User user = repository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User with username=" + username + "not found"
                ));
        return mapper.toDto(user);
    }

    @Override
    @Transactional
    public UserResponseDto create(UserRequestDto request, RoleType role) {

        log.info("Creating user with username={}", request.getUsername());

        if(repository.existsByUsername(request.getUsername())){
            throw new EntityAlreadyExistsException(
                    "User with username=" + request.getUsername() + " already exists"
            );
        }
        if (repository.existsByEmail(request.getEmail())) {
            throw new EntityAlreadyExistsException(
                    "User with email=" + request.getEmail() + " already exists"
            );
        }
        User user = mapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Set.of(role));

        User saved = repository.save(user);
        statisticsEventProducer.sendUserRegistered(saved.getId());
        return mapper.toDto(saved);
    }

    @Override
    @Transactional
    public UserResponseDto update(Long id, UserRequestDto request) {

        log.info("Updating user with id={}", id);

        User user = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User with id=" + id + " not found"
                ));

        if(!user.getUsername().equals(request.getUsername())
        && repository.existsByUsername(request.getUsername())) {
            throw new EntityAlreadyExistsException(
                    "User with username" + request.getUsername() + "already exists"
            );
        }

        if(!user.getEmail().equals(request.getEmail())
        && repository.existsByEmail(request.getEmail())) {
            throw new EntityAlreadyExistsException(
                    "Email with email" + request.getEmail() + "already exists"
            );
        }
        mapper.updateEntityFromDto(request, user);

        User saved = repository.save(user);

        return mapper.toDto(saved);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {

        User user = repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "User with id=" + id + " not found"
                        )
                );

        repository.delete(user);

    }
}
