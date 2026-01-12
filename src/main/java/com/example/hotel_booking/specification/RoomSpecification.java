package com.example.hotel_booking.specification;

import com.example.hotel_booking.model.entity.Booking;
import com.example.hotel_booking.model.entity.Room;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RoomSpecification {

    public static Specification<Room> hasId(Long id) {
        return (root, query, criteriaBuilder) ->
                id == null ? null : criteriaBuilder.equal(root.get("id"), id);
    }

    ;

    public static Specification<Room> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                name == null || name.isBlank() ? null :
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                                "%" + name.toLowerCase() + "%");
    }

    public static Specification<Room> hasMinPrice(BigDecimal minPrice) {
        return (root, query, criteriaBuilder) ->
                minPrice == null ? null :
                        criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public static Specification<Room> hasMaxPrice(BigDecimal maxPrice) {
        return (root, query, criteriaBuilder) ->
                maxPrice == null ? null :
                        criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
    }

    public static Specification<Room> hasMaxPeople(Integer maxPeople) {
        return (root, query, criteriaBuilder) ->
                maxPeople == null ? null :
                        criteriaBuilder.greaterThanOrEqualTo(root.get("maxPeople"), maxPeople);
    }

    public static Specification<Room> hasHotelId(Long hotelId) {
        return (root, query, criteriaBuilder) ->
                hotelId == null ? null :
                        criteriaBuilder.equal(root.get("hotel").get("id"), hotelId);
    }

    public static Specification<Room> isAvailableBetween(
            LocalDate checkIn,
            LocalDate checkOut
    ) {
        return (root, query, criteriaBuilder) -> {
            if (checkIn == null || checkOut == null) {
                return null;
            }
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Booking> booking = subquery.from(Booking.class);

            subquery.select(booking.get("room").get("id"))
                    .where(
                            criteriaBuilder.and(
                                    criteriaBuilder.lessThan(
                                            booking.get("checkInDate"),
                                            checkOut
                                    ),
                                    criteriaBuilder.greaterThan(
                                            booking.get("checkOutDate"),
                                            checkIn
                                    )
                            )
                    );
            return criteriaBuilder.not(root.get("id").in(subquery));

        };

    }
}


