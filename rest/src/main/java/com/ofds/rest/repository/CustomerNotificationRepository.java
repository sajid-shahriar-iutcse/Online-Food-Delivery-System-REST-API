package com.ofds.rest.repository;

import com.ofds.rest.entity.CustomerNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerNotificationRepository extends JpaRepository<CustomerNotification,Long> {
    //when should notifications be given ->
    //      when order is delivered by the restaurant
    //      when order is cancelled by the restaurant

    @Query(
            nativeQuery = true,
            value = "select * from customer_notification where customer_id= :customerId " +
                    "order by notification_time desc"
    )
    List<CustomerNotification> findAllByCustomerId(@Param("customerId") Long customerId);
}
