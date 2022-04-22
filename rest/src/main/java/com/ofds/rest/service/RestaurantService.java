package com.ofds.rest.service;

import com.ofds.rest.entity.*;
import com.ofds.rest.repository.*;
import com.ofds.rest.utility.Encrypt;
import com.ofds.rest.utility.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService {
    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    DishRepository dishRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CustomerNotificationRepository customerNotificationRepository;

    @Autowired
    RestaurantNotificationRepository restaurantNotificationRepository;


    public List<Restaurant> getListOfRestaurants() {
        return restaurantRepository.findAll();
    }

    public Restaurant getRestaurantById(Long customerId) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(customerId);
        if(restaurant.isPresent()){
            return restaurant.get();
        }
        else{
            return null;
        }
    }

    public List<Dish> getDishesByRestaurantId(Long restaurantId) {
        return dishRepository.getDishesByRestaurantId(restaurantId);
    }

    /**
     *
     * @param dish The dish to be added
     * @param restaurantId the restaurant who is going to add the dish.
     *
     *  Adds a dish to the
     *
     */
    public void addDish(Dish dish, Long restaurantId) {
        //check if restaurant exists.
        Optional<Restaurant> restaurantByRestaurantId = restaurantRepository.findById(restaurantId);

        //if exists then
        if(restaurantByRestaurantId.isPresent()){
            //if the dish name is matching another dish name of the restaurant
            Optional<Dish> dishByDishName = dishRepository.findByDishNameAndRestaurantId(dish.getDishName(),restaurantByRestaurantId.get().getRestaurantId());
            //if not
            if(!dishByDishName.isPresent()){
                 dish.setRestaurant(restaurantByRestaurantId.get());
                 dishRepository.save(dish);
            }
        }
    }

    @Transactional
    public void removeDish(Long dishId,Long restaurantId){

        Optional<Dish> dishByDishId = dishRepository.findById(dishId);

        if(dishByDishId.isPresent()){
            if(dishByDishId.get().getRestaurant().getRestaurantId() == restaurantId){
                dishByDishId.get().setDeleted(true);
                List<Cart> cartByDishId = cartRepository.findOrderedAndActiveCartByDishId(dishByDishId.get().getDishId());

                if(cartByDishId != null){
                    for(Cart cart : cartByDishId){

                        if(cart.getStatus() == cartRepository.IN_CART_STATUS){
                            cart.setStatus(cartRepository.DELETED_STATUS);
                            cart.setOrderTime(LocalDateTime.now());
                            //send notification to the user
                            String notification = cart.getDish().getRestaurant().getRestaurantName() +
                                    " is not serving " + cart.getDish().getDishName() + " anymore";

                            CustomerNotification customerNotification =
                                    CustomerNotification.builder()
                                            .notificationTime(LocalDateTime.now())
                                            .notificationMessage(notification)
                                            .customer(cart.getCustomer())
                                            .build();
                            customerNotificationRepository.save(customerNotification);


                        }
                        else if(cart.getStatus() == cartRepository.ORDERED_STATUS){
                            cart.setStatus(cartRepository.DELETED_STATUS);
                            cart.setOrderTime(LocalDateTime.now());

                            //send notification to the customer
                            String notification = "Order of " + cart.getDish().getDishName() + " from " +
                                    cart.getDish().getRestaurant().getRestaurantName() + "has been cancelled at " + cart.getOrderTime().toString();

                            CustomerNotification customerNotification = CustomerNotification.builder()
                                    .customer(cart.getCustomer())
                                    .notificationMessage(notification)
                                    .notificationTime(LocalDateTime.now())
                                    .build();

                            customerNotificationRepository.save(customerNotification);
                        }
                    }
                }
            }
        }
    }

    @Transactional
    public void deliverOrder(Long cartId, Long restaurantId) {
        Optional<Cart> cartByCartId = cartRepository.findById(cartId);

        if(cartByCartId.isPresent()){
            if(cartByCartId.get().getDish().getRestaurant().getRestaurantId() == restaurantId){
                if(cartByCartId.get().getStatus() == cartRepository.ORDERED_STATUS){
                    cartByCartId.get().setStatus(cartRepository.DELIVERED_STATUS);
                    cartByCartId.get().setOrderTime(LocalDateTime.now());

                    //Save Notification to CustomerNotificationRepository

                    String customerNotificationMessage = "Your order of "
                            + cartByCartId.get().getQuantity() + " "
                            + cartByCartId.get().getDish().getDishName()
                            + "has been delivered by "
                            + cartByCartId.get().getDish().getRestaurant();

                    CustomerNotification customerNotification = CustomerNotification.builder()
                            .customer(cartByCartId.get().getCustomer())
                            .notificationMessage(customerNotificationMessage)
                            .notificationTime(LocalDateTime.now())
                            .build();
                    customerNotificationRepository.save(customerNotification);
                }
            }
        }
    }

    @Transactional
    public void cancelOrder(Long cartId,Long restaurantId) {
        Optional<Cart> cartByCartId = cartRepository.findById(cartId);

        if(cartByCartId.isPresent()){
            if(cartByCartId.get().getDish().getRestaurant().getRestaurantId() == restaurantId){
                if(cartByCartId.get().getStatus() == cartRepository.ORDERED_STATUS){
                    cartByCartId.get().setStatus(cartRepository.CANCELLED_STATUS);

                    //Save Notification to CustomerNotificationRepository
                    String customerNotificationMessage = "Your order of "
                            + cartByCartId.get().getQuantity()
                            + cartByCartId.get().getDish().getDishName()
                            + "has been cancelled by "
                            + cartByCartId.get().getDish().getRestaurant().getRestaurantName();

                    CustomerNotification customerNotification = CustomerNotification.builder()
                            .customer(cartByCartId.get().getCustomer())
                            .notificationMessage(customerNotificationMessage)
                            .notificationTime(LocalDateTime.now())
                            .build();
                    customerNotificationRepository.save(customerNotification);

                }
            }
        }
    }

    /**
     *
     * @param restaurantId
     * @return List of orders of that restaurant
     */
    public List<Cart> getOrderList(Long restaurantId) {

        return cartRepository.findOrderedCartByRestaurantId(restaurantId);

    }

    public List<RestaurantNotification> getNotifications(Long restaurantId) {
        return restaurantNotificationRepository.findAllByRestaurantId(restaurantId);
    }

    public void registerRestaurant(Restaurant restaurant) {
        if(Validate.isValidEmail(restaurant.getRestaurantEmailId())){
            Optional<Restaurant> restaurantByEmailId =
                    restaurantRepository.findByRestaurantEmailIdIgnoreCase(restaurant.getRestaurantEmailId());
            Optional<Restaurant> restaurantByRestaurantName =
                    restaurantRepository.findByRestaurantName(restaurant.getRestaurantName());

            if(!restaurantByEmailId.isPresent() && !restaurantByRestaurantName.isPresent()){
                restaurant.setPassword(Encrypt.encryptPassword(restaurant.getPassword()));
                restaurantRepository.save(restaurant);
            }
        }
    }

    public Restaurant validateRestaurant(String restaurantName, String password) {
        Optional<Restaurant> restaurantByRestaurantName = restaurantRepository.findByRestaurantNameIgnoreCase(restaurantName);
        if(restaurantByRestaurantName.isPresent()){
            if(restaurantByRestaurantName.get().getPassword().equals(Encrypt.encryptPassword(password))){
                return restaurantByRestaurantName.get();
            }
            else{
                return null;
            }
        }
        else{
            return null;
        }
    }
}
