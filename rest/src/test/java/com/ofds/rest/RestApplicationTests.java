package com.ofds.rest;

import com.ofds.rest.entity.Cart;
import com.ofds.rest.entity.Customer;
import com.ofds.rest.entity.Dish;
import com.ofds.rest.entity.Restaurant;
import com.ofds.rest.repository.CartRepository;
import com.ofds.rest.repository.CustomerRepository;
import com.ofds.rest.repository.DishRepository;
import com.ofds.rest.repository.RestaurantRepository;
import com.ofds.rest.utility.Encrypt;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
class RestApplicationTests {

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	RestaurantRepository restaurantRepository;

	@Autowired
	DishRepository dishRepository;

	@Autowired
	CartRepository cartRepository;


	@Test
	void contextLoads() {
	}

	@Test
	public void AddCustomer() {
		Customer customer = Customer.builder()
				.customerName("Raihan")
				.emailId("Raihan@gmail.com")
				.password(Encrypt.encryptPassword("123456"))
				.build();

		customerRepository.save(customer);
	}

	@Test
	public void addRestaurant(){
		Restaurant restaurant = Restaurant.builder()
				.restaurantName("O2")
				.restaurantAddress("Dhanmondi")
				.restaurantEmailId("haters_ra_bolbe_oxygen@gmail.com")
				.password(Encrypt.encryptPassword("abcdefg"))
				.build();

		restaurantRepository.save(restaurant);
	}

	@Test
	public void addDish(){
		Optional<Restaurant> restaurantById = restaurantRepository.findById(Long.valueOf(1));
		if(restaurantById.isPresent()){
			Restaurant restaurant = restaurantById.get();
			Dish dish = Dish.builder()
					.dishName("Ratatoullie")
					.dishDescription("Man!!!")
					.dishPrice("350")
					.restaurant(restaurant)
					.build();

			dishRepository.save(dish);
		}
		else{
			System.out.println("Restaurant Does not exist");
		}

	}

	@Test
	public void removeDish(){
		dishRepository.deleteById(2L);
	}

	@Test
	public void addToCart(){
		Dish dish = dishRepository.getById(4L);
		Customer customer = customerRepository.getById(1L);

		Cart cart = Cart.builder()
				.dish(dish)
				.customer(customer)
				.quantity(1)
				.build();

		cartRepository.save(cart);
	}



	@Test
	public void getActiveCartByCustomerIdAndDishId(){
		Optional<Cart> activeCartByCustomerIdAndDishId = cartRepository.getActiveCartByCustomerIdAndDishId(1L,3L);
		if(activeCartByCustomerIdAndDishId.isPresent()){
			System.out.println(activeCartByCustomerIdAndDishId.get());
		}
	}

	@Test
	public void removeActiveCartFromCart(){
		//Gets the cart item which is in IN_CART_STATUS
		Optional<Cart> activeCartByDishIdAndCustomerId = cartRepository.getActiveCartByCustomerIdAndDishId(1L,3L);
		//checks whether the cart item is present.
		if(activeCartByDishIdAndCustomerId.isPresent()){
			if(activeCartByDishIdAndCustomerId.get().getQuantity() > 1){
				activeCartByDishIdAndCustomerId.get().setQuantity(
						activeCartByDishIdAndCustomerId.get().getQuantity() - 1
				);
				System.out.println(activeCartByDishIdAndCustomerId.get());
			}
			else if(activeCartByDishIdAndCustomerId.get().getQuantity() == 1){
				cartRepository.delete(activeCartByDishIdAndCustomerId.get());
			}
		}
	}

	@Test
	public void getActiveCartFromCartByCustomerId(){
		List<Cart> activeCartsByCustomerId = cartRepository.findActiveCartByCustomerId(3L);

		if(activeCartsByCustomerId != null){
			for(Cart cart : activeCartsByCustomerId){
				System.out.println(cart);
			}
		}
	}

	@Test
	public void findOrderedCartFromRestaurantId(){
		List<Cart> orderList = cartRepository.findOrderedCartByRestaurantId(1L);
		if(orderList != null){
			for(Cart order: orderList){
				System.out.println(order);
			}
		}
	}





}
