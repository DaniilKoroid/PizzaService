package ua.rd.pizzaservice;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ua.rd.pizzaservice.domain.Address;
import ua.rd.pizzaservice.domain.Customer;
import ua.rd.pizzaservice.domain.Order;
import ua.rd.pizzaservice.service.order.OrderService;

public class JPAWithSpringApp {

	public static void main(String[] args) {
		ConfigurableApplicationContext repContext = null;
		ConfigurableApplicationContext appContext = null;

		try {
			repContext = new ClassPathXmlApplicationContext("repositoryMySQLContext.xml");
			repContext.refresh();
			appContext = new ClassPathXmlApplicationContext(new String[] { "appContext.xml" }, repContext);
			
			OrderService orderService = appContext.getBean(OrderService.class);
			Customer customer = new Customer();
			customer.setName("Bob");
			Address address = new Address();
			address.setCountry("Kongo");
			customer.addAddress(address);
			
			Integer[] pizzasID = new Integer[]{2, 2};
			
			Order newOrder = orderService.placeNewOrder(customer, address, pizzasID);
			System.out.println("New order: " + newOrder);
		} finally {
			repContext.close();
			appContext.close();
		}
	}

}
