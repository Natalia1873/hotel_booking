package com.example.hotel_booking.specification;

import com.example.hotel_booking.model.entity.Hotel;
import org.springframework.data.jpa.domain.Specification;

public class HotelSpecification {

    public static Specification<Hotel> hasId(Long id) {
        return (root, query, criteriaBuilder) -> {
            if (id == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("id"), id);
        };
    }

    public static Specification<Hotel> hasName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null) {
                return null;
            }
            return criteriaBuilder.like
                    (criteriaBuilder.lower(root.get("name")),
                            "%" + name.toLowerCase() + "%"
                    );
        };
    }

    public static Specification<Hotel> hasAdTitle(String adTitle) {
        return (root, query, criteriaBuilder) -> {
            if (adTitle == null) {
                return null;
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("adTitle")),
                    "%" + adTitle.toLowerCase() + "%"
            );
        };
    }

    public static Specification<Hotel> hasCity(String city) {
        return (root, query, criteriaBuilder) -> {
            if(city == null){
                return null;
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("city")),
                    "%" + city.toLowerCase() + "%"
            );
        };
    }

    public static Specification<Hotel> hasAddress(String address) {
        return  (root, query, criteriaBuilder) -> {
            if( address == null){
                return  null;
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("address")),
                    "%" + address.toLowerCase() + "%"
            );
        };
    }

    public static Specification<Hotel> hasDistanceFromCenter(Double distance) {
        return  (root, query, criteriaBuilder) ->
                distance == null ? null :
                        criteriaBuilder.lessThanOrEqualTo(root.get("distanceFromCenter"),distance);
        }


    public static Specification<Hotel> hasRating(Double rating){
        return (root, query, criteriaBuilder) ->
                rating == null ? null :
                        criteriaBuilder.greaterThanOrEqualTo(root.get("rating"), rating);

    }

    public static Specification<Hotel> hasNumberOfRatings(Integer numberOfRatings) {
        return  (root, query, criteriaBuilder) ->
                numberOfRatings == null ? null :
                        criteriaBuilder.greaterThanOrEqualTo(root.get("numberOfRatings"), numberOfRatings);
    }




}





