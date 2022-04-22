package com.ofds.rest.entity;

import com.ofds.rest.repository.CartRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Cart {
    @Id
    @SequenceGenerator(
            name="cart_sequence",
            sequenceName = "cart_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cart_sequence"
    )
    private Long cartId;

    @ManyToOne
    @JoinColumn(
            name = "dish_id",
            referencedColumnName = "dishId"
    )
    private Dish dish;
    @ManyToOne()
    @JoinColumn(
            name= "customer_id",
            referencedColumnName = "customerId"
    )
    private Customer customer;
    private int quantity;
    private int status = CartRepository.IN_CART_STATUS;
    private LocalDateTime orderTime;

}
