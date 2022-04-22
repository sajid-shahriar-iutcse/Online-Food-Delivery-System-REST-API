package com.ofds.rest.service;

import com.ofds.rest.entity.*;
import com.ofds.rest.repository.*;
import com.ofds.rest.utility.Encrypt;
import com.ofds.rest.utility.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private RestaurantNotificationRepository restaurantNotificationRepository;

    @Autowired
    private CustomerNotificationRepository customerNotificationRepository;

    /**
     *
     * @param customer The Customer object that is to be saved in the database.
     *  Registers a valid customer.
     */
    public void registerCustomer(Customer customer) {
        if(Validate.isValidEmail(customer.getEmailId())){
            Optional<Customer> customerByCustomerName =
                    customerRepository.findByCustomerNameIgnoreCase(customer.getCustomerName());

            Optional<Customer> customerByEmailId =
                    customerRepository.findByEmailIdIgnoreCase(customer.getEmailId());

            if(!customerByCustomerName.isPresent() && !customerByEmailId.isPresent()){
                customer.setPassword(Encrypt.encryptPassword(customer.getPassword()));
                customerRepository.save(customer);

            }
        }
    }

    /**
     *
     * @param emailId email id of the customer.
     * @param password the password provided by the customer.
     * @return the customer info if the customer exists.
     */
    public Customer validateCustomer(String emailId, String password) {

        Optional<Customer> customerByEmailId = customerRepository.findByEmailIdIgnoreCase(emailId);
        Customer customer = null;
        if(customerByEmailId.isPresent()){
            customer  = customerByEmailId.get();

        }
        else{
            return null;
        }

        if(customer.getPassword().equals(Encrypt.encryptPassword(password))){
            return customer;
        }
        else{
            return null;
        }

    }

    public List<Customer> getListOfCustomers() {

        return  customerRepository.findAll();
    }

    public List<Restaurant> getListOfRestaurants(){
        return restaurantRepository.findAll();
    }

    /**
     *
     * @param searchString is the substring that is present in restaurant name.
     * @return the list of restaurants whose name contain the search string.
     */
    public List<Restaurant> getRestaurantsByRestaurantNameContaining(String searchString){
        return restaurantRepository.findAllByRestaurantNameContaining(searchString);
    }

    /**
     *
     * @param restaurantId the id of the restaurant whose menu is going to be returned.
     * @return the dishes(Menu) of the restaurant identified by restaurantId.
     */
    public List<Dish> getListOfDishesFromRestaurantId(Long restaurantId) {
        return dishRepository.getDishesByRestaurantId(restaurantId);
    }

    /**
     *
     * @param dishId the id of the dish which is going to be added to cart.
     * @param customerId the id of the customer who is going to add the dish to the cart.
     *
     *                   successful
     */
    @Transactional
    public void addToCart(Long dishId,Long customerId){

        //Gets the cart item if already exists and in IN_CART_STATUS
        Optional<Cart> activeCartByDishIdAndCustomerId = cartRepository.getActiveCartByCustomerIdAndDishId(dishId,customerId);

        //If the cart is present, and in IN_CART_STATUS then increase the quantity.
        if(activeCartByDishIdAndCustomerId.isPresent()){
            activeCartByDishIdAndCustomerId.get().setQuantity(
                    activeCartByDishIdAndCustomerId.get().getQuantity() + 1
            );
        }
        else{
            //Gets the customer and dish if exists.
            Optional<Customer> customerByCustomerId = customerRepository.findById(customerId);
            Optional<Dish> dishByDishId = dishRepository.findById(dishId);

            //checks whether the Customer or Dish does not exist
            if(!customerByCustomerId.isPresent() || !dishByDishId.isPresent()){

                //if not then do nothing.
                return;
            }
            else{
                //else create a new cart item and save it to the cartRepository.
                Cart cartToBeSaved = Cart.builder()
                        .quantity(1)
                        .dish(dishByDishId.get())
                        .customer(customerByCustomerId.get())
                        .build();
                cartRepository.save(cartToBeSaved);
            }
        }
    }

    public List<Cart> getActiveCartsByCustomerId(Long customerId) {

        return cartRepository.findActiveCartByCustomerId(customerId);
    }


    /**
     *
     * @param dishId id of the dish which is going to be removed from the cart.
     * @param customerId the id of the customer who is going to remove it from the cart.
     * If cart item quantity is more than one then it removes one unit of cart item. If the cart item
     * quantity is equal to one then it removes the cart item from the cart repository.
     */
    @Transactional
    public void removeActiveCartFromCart(Long dishId, Long customerId) {
        //Gets the cart item which is in IN_CART_STATUS
        Optional<Cart> activeCartByDishIdAndCustomerId = cartRepository.getActiveCartByCustomerIdAndDishId(dishId,customerId);
        //checks whether the cart item is present.
        if(activeCartByDishIdAndCustomerId.isPresent()){
            if(activeCartByDishIdAndCustomerId.get().getQuantity() > 1){
                activeCartByDishIdAndCustomerId.get().setQuantity(activeCartByDishIdAndCustomerId.get().getQuantity() - 1);
            }
            else if(activeCartByDishIdAndCustomerId.get().getQuantity() == 1){
                cartRepository.delete(activeCartByDishIdAndCustomerId.get());
            }
        }
    }

    /**
     *
     * @param customerId id of the customer who is confirming his/her order.
     * Changes the cart status which are in IN_CART_STATUS  to ORDERED_STATUS
     */
    @Transactional
    public void checkout(Long customerId) {
        //Gets cart of the customer identified by customerId and the status is IN_CART_status
        List<Cart> activeCartByCustomerId = cartRepository.findActiveCartByCustomerId(customerId);

        //Checks whether the cart list is null
        if(activeCartByCustomerId != null){
            //if not then change the cart item status which are in IN_CART_STATUS to ORDERED_STATUS.
            for(Cart cart : activeCartByCustomerId){
                //change status and set time
                cart.setStatus(cartRepository.ORDERED_STATUS);
                cart.setOrderTime(LocalDateTime.now());

                //save notification in RestaurantNotificationRepository

                String notificationMessage = cart.getCustomer().getCustomerName()
                        + " has ordered " + cart.getQuantity() + " " + cart.getDish().getDishName();

                RestaurantNotification restaurantNotification = RestaurantNotification.builder()
                        .restaurant(cart.getDish().getRestaurant())
                        .notificationMessage(notificationMessage)
                        .notificationTime(LocalDateTime.now())
                        .build();

                restaurantNotificationRepository.save(restaurantNotification);
            }
        }

    }


    public List<CustomerNotification> getNotification(Long customerId) {
        return customerNotificationRepository.findAllByCustomerId(customerId);
    }
}
