package ua.rd.pizzaservice;

import ua.rd.pizzaservice.domain.customer.Customer;
import ua.rd.pizzaservice.domain.order.Order;
import ua.rd.pizzaservice.service.accumulationcard.AccumulationCardService;
import ua.rd.pizzaservice.service.accumulationcard.SimpleAccumulationCardService;
import ua.rd.pizzaservice.service.discount.DiscountService;
import ua.rd.pizzaservice.service.discount.DiscountServiceImpl;
import ua.rd.pizzaservice.service.order.OrderService;
import ua.rd.pizzaservice.service.order.SimpleOrderService;

public class PizzaApp {
	public static void main(String[] args) {
		Customer customer = new Customer("Ivan");
		Order order;

		AccumulationCardService accCardService = new SimpleAccumulationCardService();
		DiscountService discountService = new DiscountServiceImpl(accCardService);
		OrderService orderService = new SimpleOrderService(discountService, accCardService);
		order = orderService.placeNewOrder(customer, 1, 2, 3);

		System.out.println(order);
	}
}
