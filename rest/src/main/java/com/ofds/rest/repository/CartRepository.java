package com.ofds.rest.repository;

import com.ofds.rest.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {

    int IN_CART_STATUS = 0;
    int ORDERED_STATUS = 1;
    int DELIVERED_STATUS = 2;
    int CANCELLED_STATUS = 3;
    int DELETED_STATUS=4;

    @Query(
            nativeQuery = true,
            value = "select * from cart c where c.customer_id =?1 and c.status = 0"
    )
    List<Cart> findActiveCartByCustomerId(Long customerId);

    @Query(
            nativeQuery = true,
            value = "select * from cart c where c.customer_id =:customerId  and c.dish_id = :dishId  and c.status = 0"
    )
    Optional<Cart> getActiveCartByCustomerIdAndDishId(@Param("dishId") Long dishId, @Param("customerId") Long customerId);


    //here ordered does not mean sorted but a status

    @Query(
            nativeQuery = true,
            value = "select * from cart " +
                    "where cart.status = 1 and cart.dish_id " +
                    "in (select dish.dish_id from dish  where dish.restaurant_id = :restaurantId) "
    )
    List<Cart> findOrderedCartByRestaurantId(@Param("restaurantId") Long restaurantId);

    @Query(
            nativeQuery = true,
            value = "select * from cart "+
                    "where dish_id = :dishId "+
                    "and status = :1"
    )
    List<Cart> findOrderedCartByDishId(@Param("dishId")Long dishId);

    @Query(
            nativeQuery = true,
            value = "select * from cart " +
                    "where dish_id = :dishId and (status = 0 or status = 1)"
    )
    List<Cart>findOrderedAndActiveCartByDishId(@Param("dishId")Long dishId);
}
