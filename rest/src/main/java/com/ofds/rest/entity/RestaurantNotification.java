package com.ofds.rest.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantNotification {

    @Id
    @SequenceGenerator(
            name= "restaurant_notification_sequence",
            sequenceName = "restaurant_notification_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "restaurant_notification_sequence"
    )
    private Long notificationId;

    private String notificationMessage;

    @ManyToOne()
    @JoinColumn(
            name = "restaurant_id",
            referencedColumnName = "restaurantId"
    )
    private Restaurant restaurant;

    private LocalDateTime notificationTime;
}
