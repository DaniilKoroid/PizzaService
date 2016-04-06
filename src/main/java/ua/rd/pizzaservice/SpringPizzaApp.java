package ua.rd.pizzaservice;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ua.rd.pizzaservice.domain.order.Order;
import ua.rd.pizzaservice.repository.pizza.PizzaRepository;
import ua.rd.pizzaservice.service.order.OrderService;

public class SpringPizzaApp {

	public static void main(String[] args) {
		ConfigurableApplicationContext appContext = new ClassPathXmlApplicationContext("appContext.xml");
		PizzaRepository pizzaRepository = (PizzaRepository) appContext.getBean("pizzaRepository");
		OrderService orderService = (OrderService) appContext.getBean("orderService");
		System.out.println(pizzaRepository.getPizzaByID(1));
		Order order = orderService.placeNewOrder(null, 1, 2, 3);
		System.out.println(order);
		appContext.close();
	}

}
