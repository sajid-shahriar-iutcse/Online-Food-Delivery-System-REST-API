package com.ofds.rest.repository;

import com.ofds.rest.entity.RestaurantNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantNotificationRepository extends JpaRepository<RestaurantNotification,Long> {

    //When should notifications be given ->
        //When customer has ordered in a particular restaurant.

    @Query(
            nativeQuery = true,
            value = "select * from restaurant_notification "
                + "where restaurant_id = :restaurantId "
                + "order by notification_time desc"
    )
    List<RestaurantNotification> findAllByRestaurantId(@Param("restaurantId")Long restaurantId);
}
