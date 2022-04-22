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
public class CustomerNotification {



    @Id
    @SequenceGenerator(
            name= "customer_notification_sequence",
            sequenceName = "customer_notification_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "customer_notification_sequence"
    )
    private Long notificationId;
    private String notificationMessage;
    @ManyToOne()
    @JoinColumn(
            name = "customer_id",
            referencedColumnName = "customerId"
    )
    private Customer customer;

    private LocalDateTime notificationTime;
}
