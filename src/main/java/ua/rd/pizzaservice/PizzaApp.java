package ua.rd.pizzaservice;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ua.rd.pizzaservice.domain.customer.Customer;
import ua.rd.pizzaservice.domain.order.Order;
import ua.rd.pizzaservice.service.order.OrderService;

public class PizzaApp {
	public static void main(String[] args) {

		ConfigurableApplicationContext appContext = new ClassPathXmlApplicationContext("appContext.xml");

		Order order;
		Customer customer = appContext.getBean("customer", Customer.class);

		OrderService orderService = appContext.getBean("orderService", OrderService.class);
		Integer[] pizzasId = new Integer[]{1, 2, 3};
		order = orderService.placeNewOrder(customer, pizzasId);

		System.out.println(order);

		appContext.close();
	}
}
