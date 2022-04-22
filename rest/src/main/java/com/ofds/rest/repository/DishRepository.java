package com.ofds.rest.repository;

import com.ofds.rest.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DishRepository extends JpaRepository<Dish,Long> {

    @Query(
            nativeQuery = true,
            value= "select * from dish d where d.restaurant_id =?1"
    )
    List<Dish> getDishesByRestaurantId(Long restaurantId);

    @Query(
            nativeQuery = true,
            value = "select * from dish where dish.dish_name = :dishName and dish.restaurant_id = :restaurantId"
    )
    Optional<Dish> findByDishNameAndRestaurantId(@Param("dishName")String dishName, @Param("restaurantId")Long restaurantId);
}
