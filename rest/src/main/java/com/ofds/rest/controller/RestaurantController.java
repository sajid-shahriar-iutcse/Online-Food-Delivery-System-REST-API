package com.ofds.rest.controller;

import com.ofds.rest.entity.*;
import com.ofds.rest.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RestaurantController {

    @Autowired
    RestaurantService restaurantService;

    @GetMapping("api/restaurant")
    public List<Restaurant> getListOfRestaurants(){
        return restaurantService.getListOfRestaurants();
    }

    /**
     *
     * @param restaurant The JSON object which will be mapped to the restaurant object.
     * Saves the restaurant details to the database.
     *  Postman test Successful
     */
    @PostMapping("api/restaurant")
    public void registerRestaurant(@RequestBody Restaurant restaurant){
        restaurantService.registerRestaurant(restaurant);

    }

    /**
     *
     * @param restaurantName the name of the restaurant to be searched.
     * @param password
     * @return the restaurant if password and restaurant name is correct
     * Postman Test successful
     */
    @GetMapping("api/restaurant/{restaurantName}/{password}")
    public Restaurant validateRestaurant(@PathVariable("restaurantName")String restaurantName,@PathVariable("password")String password){
        return restaurantService.validateRestaurant(restaurantName,password);
    }


    /**
     *
     * @param restaurantId the id of the restaurant whose detail will be sent.
     * @return the restaurant details for the given restaurant id.
     * Not a feature
     * Postman Test Successful
     */
    @GetMapping("api/restaurant/{restaurantId}")
    public Restaurant getRestaurantById(@PathVariable("restaurantId")Long restaurantId){
        return restaurantService.getRestaurantById(restaurantId);
    }

    /**
     * @param restaurantId the id of the restaurant whose entire menu list will be called.
     * @return the Dish list for the given restaurantId.
     */
    @GetMapping("api/restaurant/dish/{restaurantId}")
    public List<Dish> getDishesByRestaurantId(@PathVariable("restaurantId")Long restaurantId){
        return restaurantService.getDishesByRestaurantId(restaurantId);
    }

    /**
     *
     * @param dish The JSON object whose details will be saved
     * @param restaurantId the id of the restaurant who is saving the dish.
     * Adds a new dish for a restaurant
     *
     * Postman test : successful
     */
    @PostMapping("api/restaurant/dish/add/{restaurantId}")
    public void addDish(@RequestBody Dish dish,@PathVariable("restaurantId")Long restaurantId ){
        restaurantService.addDish(dish,restaurantId);

    }

    /**
     * @param restaurantId the id of the restaurant whose order list is to be returned.
     * @return the list of orders which are placed to the restaurant.
     * Postman Test Successful.
     */
    @GetMapping("api/restaurant/order/{restaurantId}")
    public List<Cart> getOrderList(@PathVariable("restaurantId")Long restaurantId){
        return restaurantService.getOrderList(restaurantId);
    }

    /**
     *
     * @param dishId the id of the dish which is going to be removed.
     * @param restaurantId the id of the restaurant who is going to remove the dish.
     * Deletes the item from the menu.
     */

    @PutMapping("api/restaurant/dish/remove/{restaurantId}/{dishId}")
    public void removeDish(@PathVariable("dishId")Long dishId,@PathVariable("restaurantId") Long restaurantId){
        restaurantService.removeDish(dishId,restaurantId);
    }

    /**
     *
     * @param cartId the cart id whose status is going to be changed.
     * @param restaurantId  the restaurant who is going to deliver the dish.
     * Changes the status of the cart item from ORDERED_STATUS to DELIVERED_STATUS(Soft delete)
     * Postman Test Successful.
     */
    @PutMapping("api/restaurant/cart/deliver/{cartId}/{restaurantId}")
    public void deliverOrder(@PathVariable("cartId") Long cartId,@PathVariable("restaurantId")Long restaurantId){
        restaurantService.deliverOrder(cartId,restaurantId);
    }

    /**
     *
     * @param cartId the id of the cart item whose status is going to be changed.
     * @param restaurantId the id of restaurant who is going to cancel the dish.
     * Changes the status of the cart item from ORDERED_STATUS to CANCELLED_STATUS.
     */
    @PutMapping("api/restaurant/cart/cancel/{cartId}/{restaurantId}")
    public void cancelOrder(@PathVariable("cartId")Long cartId,@PathVariable("restaurantId")Long restaurantId){
        restaurantService.cancelOrder(cartId,restaurantId);

    }

    /**
     * @param restaurantId the id of the restaurant who wiil be getting the notification.
     * @return the list of notification sorted by time from latest to oldest.
     */

    @GetMapping("api/restaurant/notification/{restaurantId}")
    public List<RestaurantNotification> getNotifications(@PathVariable("restaurantId") Long restaurantId){
        return restaurantService.getNotifications(restaurantId);
    }
}
