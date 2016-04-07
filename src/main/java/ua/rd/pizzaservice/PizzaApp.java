package ua.rd.pizzaservice;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ua.rd.pizzaservice.domain.customer.Customer;
import ua.rd.pizzaservice.domain.order.Order;
import ua.rd.pizzaservice.repository.order.OrderRepository;
import ua.rd.pizzaservice.repository.pizza.PizzaRepository;
import ua.rd.pizzaservice.service.accumulationcard.AccumulationCardService;
import ua.rd.pizzaservice.service.accumulationcard.SimpleAccumulationCardService;
import ua.rd.pizzaservice.service.discount.DiscountProvider;
import ua.rd.pizzaservice.service.discount.DiscountService;
import ua.rd.pizzaservice.service.discount.SimpleDiscountService;
import ua.rd.pizzaservice.service.order.OrderService;
import ua.rd.pizzaservice.service.order.SimpleOrderService;

public class PizzaApp {
	public static void main(String[] args) {
		
		ConfigurableApplicationContext appContext = new ClassPathXmlApplicationContext("appContext.xml");
		
		
		Customer customer = new Customer("Ivan");
		Order order;

		PizzaRepository pizzaRepository = (PizzaRepository) appContext.getBean("pizzaRepository");
		OrderRepository orderRepository = (OrderRepository) appContext.getBean("orderRepository");
		DiscountProvider discountProvider = (DiscountProvider) appContext.getBean("discountProvider");
		AccumulationCardService accCardService = (AccumulationCardService) appContext.getBean("accumulationCardService");
		DiscountService discountService = (DiscountService) appContext.getBean("discountService");
		OrderService orderService = (OrderService) appContext.getBean("orderService");
		accCardService = new SimpleAccumulationCardService();
		discountService = new SimpleDiscountService(accCardService, discountProvider);
		orderService = new SimpleOrderService(discountService, accCardService, pizzaRepository, orderRepository);
		Integer[] pizzasId = new Integer[]{1, 2, 3};
		order = orderService.placeNewOrder(customer, pizzasId);

		System.out.println(order);
		
		appContext.close();
	}
}
