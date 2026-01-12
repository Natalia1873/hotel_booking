package com.example.hotel_booking.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // users
                        .requestMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/v1/users/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/users/**").hasAnyRole("USER", "ADMIN")

                        // hotels
                        .requestMatchers(HttpMethod.POST,"/api/v1/hotels/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/v1/hotels/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/hotels/**").hasRole("ADMIN")

                        // rooms
                        .requestMatchers(HttpMethod.POST,"/api/v1/rooms/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/v1/rooms/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/rooms/**").hasRole("ADMIN")

                        // other
                        .requestMatchers(HttpMethod.GET,"/api/v1/bookings").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/statistics/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )

                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
