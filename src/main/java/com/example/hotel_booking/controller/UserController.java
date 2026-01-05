package com.example.hotel_booking.controller;


import com.example.hotel_booking.model.dto.UserRequestDto;
import com.example.hotel_booking.model.dto.UserResponseDto;
import com.example.hotel_booking.model.enums.RoleType;
import com.example.hotel_booking.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController {

    private final UserService service;

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers(){
        log.info("Fetching all users");
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id){
        log.info("Fetching user with id={}", id);
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/by-username/{username}")
    public ResponseEntity<UserResponseDto> getUserByUsername(@PathVariable String username){
        log.info("Fetching user with username={}", username);
        return ResponseEntity.ok(service.findByUsername(username));
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(
            @Valid @RequestBody UserRequestDto request,
            @RequestParam RoleType role
    ){
        log.info("Creating user with role={}", role);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(request, role));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUserById(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDto request)
    {
        log.info("Updating user with id={}", id);
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
