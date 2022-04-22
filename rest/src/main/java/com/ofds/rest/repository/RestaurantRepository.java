package com.ofds.rest.repository;

import com.ofds.rest.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant,Long> {

    @Query(
            nativeQuery = true,
            value = "select * from restaurant r where r.restaurant_name like %:searchString%"
    )
    List<Restaurant> findAllByRestaurantNameContaining(@Param("searchString")String searchString);

    @Query(
            nativeQuery = true,
            value = "select * from restaurant where " +
                    "restaurant_name = :restaurantName"
    )
    Optional<Restaurant> findByRestaurantName(@Param("restaurantName")String restaurantName);

    Optional<Restaurant> findByRestaurantEmailIdIgnoreCase(String restaurantEmailId);

    Optional<Restaurant> findByRestaurantNameIgnoreCase(String restaurantName);
}
