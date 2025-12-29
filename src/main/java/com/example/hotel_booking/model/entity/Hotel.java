package com.example.hotel_booking.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name="hotels")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Hotel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name="ad_title")
    private String adTitle;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String address;

    @Column(name="distance_from_center")
    private Double distanceFromCenter;

    @Min(1)
    @Max(5)
    private Double rating;

    @Column(name="number_of_ratings")
    private Integer numberOfRatings;

    private static final long serialVersionUID = 1L;

}
