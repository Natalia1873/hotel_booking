package com.example.controller;

import com.example.hotel_booking.HotelBookingApplication;
import com.example.hotel_booking.config.SecurityConfig;
import com.example.hotel_booking.controller.UserController;
import com.example.hotel_booking.model.dto.UserRequestDto;
import com.example.hotel_booking.model.dto.UserResponseDto;
import com.example.hotel_booking.model.enums.RoleType;
import com.example.hotel_booking.service.UserService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
@ContextConfiguration(classes = HotelBookingApplication.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService service;

    // ---------- GET ALL ----------

    @Test
    @WithMockUser
    void getAllUsers_success() throws Exception {
        UserResponseDto user = new UserResponseDto();
        user.setId(1L);
        user.setUsername("user1");
        user.setEmail("user1@mail.com");

        when(service.findAll()).thenReturn(List.of(user));

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[0].email").value("user1@mail.com"));
    }

    // ---------- GET BY ID ----------

    @Test
    @WithMockUser
    void getUserById_success() throws Exception {
        UserResponseDto user = new UserResponseDto();
        user.setId(1L);
        user.setUsername("user1");

        when(service.findById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("user1"));
    }

    // ---------- GET BY USERNAME ----------

    @Test
    @WithMockUser
    void getUserByUsername_success() throws Exception {
        UserResponseDto user = new UserResponseDto();
        user.setId(1L);
        user.setUsername("user1");

        when(service.findByUsername("user1")).thenReturn(user);

        mockMvc.perform(get("/api/v1/users/by-username/user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user1"));
    }

    // ---------- CREATE ----------

    @Test
    void createUser_success() throws Exception {
        UserResponseDto response = new UserResponseDto();
        response.setId(1L);
        response.setUsername("user1");
        response.setEmail("user1@mail.com");

        when(service.create(any(UserRequestDto.class), eq(RoleType.ROLE_USER)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/users")
                        .param("role", "ROLE_USER")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "username": "user1",
                      "email": "user1@mail.com",
                      "password": "password"
                    }
                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("user1"))
                .andExpect(jsonPath("$.email").value("user1@mail.com"));
    }

    // ---------- CREATE VALIDATION ----------

    @Test
    void createUser_invalidRequest_shouldReturn400() throws Exception {
        mockMvc.perform(post("/api/v1/users")
                        .param("role", "ROLE_USER")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "username": "",
                      "email": "not-email"
                    }
                """))
                .andExpect(status().isBadRequest());
    }

    // ---------- UPDATE ----------

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateUser_success() throws Exception {
        UserResponseDto response = new UserResponseDto();
        response.setId(1L);
        response.setUsername("updated");

        when(service.update(eq(1L), any(UserRequestDto.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "username": "updated",
                      "email": "updated@mail.com",
                      "password": "password"
                    }
                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("updated"));
    }

    // ---------- DELETE ----------

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteUser_success() throws Exception {
        doNothing().when(service).deleteById(1L);

        mockMvc.perform(delete("/api/v1/users/1"))
                .andExpect(status().isNoContent());
    }

    // ---------- SECURITY ----------

    @Test
    void getAllUsers_unauthorized_shouldReturn401() throws Exception {
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isUnauthorized());
    }
}

