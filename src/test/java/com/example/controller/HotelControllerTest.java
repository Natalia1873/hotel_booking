package com.example.controller;

import com.example.hotel_booking.HotelBookingApplication;
import com.example.hotel_booking.config.SecurityConfig;
import com.example.hotel_booking.controller.HotelController;
import com.example.hotel_booking.model.dto.HotelPageResponseDto;
import com.example.hotel_booking.model.dto.HotelRequestDto;
import com.example.hotel_booking.model.dto.HotelResponseDto;
import com.example.hotel_booking.service.HotelService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HotelController.class)
@Import(SecurityConfig.class)
@ContextConfiguration(classes = HotelBookingApplication.class)
class HotelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HotelService service;

    @Test
    @WithMockUser
    void getAllHotels_success() throws Exception {
        HotelPageResponseDto page = new HotelPageResponseDto();
        page.setTotalElements(1);

        when(service.findAll(any())).thenReturn(page);

        mockMvc.perform(get("/api/v1/hotels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @WithMockUser
    void getHotelById_success() throws Exception {
        HotelResponseDto hotel = new HotelResponseDto();
        hotel.setId(1L);
        hotel.setName("Test Hotel");

        when(service.findById(1L)).thenReturn(hotel);

        mockMvc.perform(get("/api/v1/hotels/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Hotel"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createHotel_success() throws Exception {
        HotelResponseDto response = new HotelResponseDto();
        response.setId(1L);
        response.setName("New Hotel");

        when(service.create(any(HotelRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "name": "New Hotel",
                      "address": "Barcelona",
                      "city": "Barcelona",
                      "description": "Nice hotel"
                    }
                    """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("New Hotel"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateRating_success() throws Exception {
        HotelResponseDto response = new HotelResponseDto();
        response.setId(1L);
        response.setRating(5.0);

        when(service.updateRating(eq(1L), eq(5)))
                .thenReturn(response);

        mockMvc.perform(patch("/api/v1/hotels/1/rating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            { "newMark": 5 }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(5.0));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteHotel_success() throws Exception {
        doNothing().when(service).deleteById(1L);

        mockMvc.perform(delete("/api/v1/hotels/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER") // залогинен, но не ADMIN
    void createHotel_forbidden_shouldReturn403() throws Exception {
        mockMvc.perform(post("/api/v1/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createHotel_validationError_shouldReturn400() throws Exception {
        mockMvc.perform(post("/api/v1/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "name": "",
                      "city": ""
                    }
                    """))
                .andExpect(status().isBadRequest());
    }

}
