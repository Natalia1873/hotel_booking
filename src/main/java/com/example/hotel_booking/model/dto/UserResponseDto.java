package com.example.hotel_booking.model.dto;

import com.example.hotel_booking.model.enums.RoleType;
import lombok.Data;
import java.util.Set;

@Data
public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    private Set<RoleType> roles;

}
