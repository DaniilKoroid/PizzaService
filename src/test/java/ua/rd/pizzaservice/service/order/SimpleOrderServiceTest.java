package ua.rd.pizzaservice.service.order;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import ua.rd.pizzaservice.domain.customer.Customer;
import ua.rd.pizzaservice.domain.order.Order;

public class SimpleOrderServiceTest {

	Customer customer;
	OrderService orderService;
	
	@Before
	public void setUpCustomer() {
		customer = new Customer();
	}
	
	@Before
	public void setUpOrderService() {
		orderService = new SimpleOrderService();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testPlaceNewOrderWithTooMuchPizzas() {
		int length = 11;
		Integer[] arr = new Integer[length];
		orderService.placeNewOrder(customer, arr);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testPlaceNewOrderWithZeroPizzas() {
		int length = 0;
		Integer[] arr = new Integer[length];
		orderService.placeNewOrder(customer, arr);
	}
	
	@Test
	public void testPlaceNewOrderWithAppropriatePizzasCount() {
		Integer[] pizzasID = new Integer[]{1, 2, 3, 3, 2, 1};
		int expectedOrderSize = pizzasID.length;
		Order newOrder = orderService.placeNewOrder(customer, pizzasID);
		int orderSize = newOrder.getPizzas().size();
		assertEquals(expectedOrderSize, orderSize);
	}

}
