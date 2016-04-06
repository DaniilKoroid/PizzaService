package ua.rd.pizzaservice;

import ua.rd.pizzaservice.domain.customer.Customer;
import ua.rd.pizzaservice.domain.order.Order;
import ua.rd.pizzaservice.service.accumulationcard.AccumulationCardService;
import ua.rd.pizzaservice.service.accumulationcard.SimpleAccumulationCardService;
import ua.rd.pizzaservice.service.discount.DiscountService;
import ua.rd.pizzaservice.service.discount.SimpleDiscountService;
import ua.rd.pizzaservice.service.order.OrderService;
import ua.rd.pizzaservice.service.order.SimpleOrderService;

public class PizzaApp {
	public static void main(String[] args) {
		Customer customer = new Customer("Ivan");
		Order order;

		AccumulationCardService accCardService;
		DiscountService discountService;
		OrderService orderService;
		accCardService = new SimpleAccumulationCardService();
		discountService = new SimpleDiscountService(accCardService);
		orderService = new SimpleOrderService(discountService, accCardService);
		Integer[] pizzasId = new Integer[]{1, 2, 3};
		order = orderService.placeNewOrder(customer, pizzasId);

		System.out.println(order);
	}
}
