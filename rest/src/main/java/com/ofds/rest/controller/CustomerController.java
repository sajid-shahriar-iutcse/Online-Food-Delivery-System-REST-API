package com.ofds.rest.controller;


import com.ofds.rest.entity.*;
import com.ofds.rest.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    /**
     *
     * @return List of all the customers
     * Not really a feature but something to test the api is working
     * Postman Test Successful.
     *
     */
    @GetMapping("api/customer")
    public List<Customer> getListOfCustomers(){
        return customerService.getListOfCustomers();

    }

    /**
     * The following method is used for registering a new user.
     * @param customer is a JSON object gotten from the request body
     *
     *Postman Test Successful.
     */
    @PostMapping("api/customer")
    public void registerCustomer(@RequestBody Customer customer){

        customerService.registerCustomer(customer);
    }

    /**
     *
     * @param emailId email id of the customer
     * @param password password provided by the customer
     * @return Customer Details if credentials are right
     * Postman Test Successful.
     */
    @GetMapping("api/customer/{emailId}/{password}")
    public Customer validateCustomer(@PathVariable("emailId")String emailId, @PathVariable("password")String password){
        return customerService.validateCustomer(emailId,password);
    }

    /**
     * @return  all the restaurants
     * Postman Test Successful.
     */
    @GetMapping("api/customer/restaurants")
    public List<Restaurant> getListOfRestaurants(){
        return customerService.getListOfRestaurants();
    }

    /**
     *
     * @param searchString the substring which any restaurant name can contain
     * @return list of restaurants whose name contains searchString.
     * Postman Test Successful.
     */
    @GetMapping("api/customer/restaurants/{searchString}")
    public List<Restaurant> getRestaurantsByRestaurantNameContaining(@PathVariable("searchString")String searchString){
        return customerService.getRestaurantsByRestaurantNameContaining(searchString);
    }

    /**
     * @param restaurantId uniquely identifies the restaurant.
     * @return list of dishes that are cooked in the restaurant.
     * Postman Test Successful
     */
    @GetMapping("api/customer/dishes/{restaurantId}")
    public List<Dish> getListOfDishesFromRestaurantId(@PathVariable("restaurantId")Long restaurantId){
        return customerService.getListOfDishesFromRestaurantId(restaurantId);

    }

    /**
     *
     * @param dishId The id of the dish that is to be added to the cart.
     * @param customerId The id the customer who wishes to add this to the cart.
     * Postman Test Successful.
     */

    @PutMapping("api/customer/cart/{dishId}/{customerId}")
    public void addToCart(@PathVariable("dishId") Long dishId,@PathVariable("customerId")Long customerId){
        customerService.addToCart(dishId,customerId);
    }

    /**
     *
     * @param customerId the Id of the customer whose cart is to be returned
     * @return Cart items of a customer
     * Postman Test : Successful
     */
    @GetMapping("api/customer/cart/{customerId}")
    public List<Cart> getActiveCartsByCustomerId(@PathVariable("customerId")Long customerId){
        return customerService.getActiveCartsByCustomerId(customerId);
    }

    /**
     *
     * @param dishId the id of the dish which is gonna be removed from the cart.
     * @param customerId the id of the customer from whose cart the item is going to be deleled.
     *
     *  The functions reduces a single quantity of a particular dish. If the dish quantity is 1
     *  then it removes it altogether from the cart.
     *
     *  Postman Test : Successful.
     *
     */
    @PutMapping("api/customer/cart/remove/{dishId}/{customerId}")
    public void removeActiveCartFromCart(@PathVariable("dishId")Long dishId,@PathVariable("customerId")Long customerId){
        customerService.removeActiveCartFromCart(dishId,customerId);

    }

    /**
     * @param customerId the id of the customer who wants to check out.
     * Changes the status of cart item to ORDERED_STATUS
     * Postman Test : Successful
     */

    @PutMapping("api/customer/cart/checkout/{customerId}")
    public void checkout(@PathVariable("customerId") Long customerId){
        customerService.checkout(customerId);
    }

    /**
     *
     * @param customerId the id of the customer whose notifications are going to be fetched from database.
     * @return the list of notifications that will be shown to the customer identified by customerId
     */
    @GetMapping("api/customer/notification/{customerId}")
    public List<CustomerNotification> getNotifications(@PathVariable("customerId") Long customerId){
        return customerService.getNotification(customerId);
    }
}
