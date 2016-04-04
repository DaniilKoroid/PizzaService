package ua.rd.pizzaservice;

import ua.rd.pizzaservice.domain.customer.Customer;
import ua.rd.pizzaservice.domain.order.Order;
import ua.rd.pizzaservice.service.order.OrderService;
import ua.rd.pizzaservice.service.order.SimpleOrderService;

public class PizzaApp {
	public static void main(String[] args) {
		Customer customer = null;
		Order order;

		OrderService orderService = new SimpleOrderService();
		order = orderService.placeNewOrder(customer, 1, 2, 3);

		System.out.println(order);
	}
}
