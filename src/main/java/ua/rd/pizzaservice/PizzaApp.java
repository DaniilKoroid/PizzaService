package ua.rd.pizzaservice;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ua.rd.pizzaservice.domain.Address;
import ua.rd.pizzaservice.domain.Customer;
import ua.rd.pizzaservice.domain.Order;
import ua.rd.pizzaservice.service.order.OrderService;

public class PizzaApp {
	public static void main(String[] args) {

		// TODO pagination
		
		ConfigurableApplicationContext repContext = new ClassPathXmlApplicationContext("dbRepositoryContext.xml");
		repContext.getEnvironment().setActiveProfiles("dev", "db_mysql");
		repContext.refresh();
		ConfigurableApplicationContext appContext = new ClassPathXmlApplicationContext(
				new String[] { "appContext.xml" }, repContext);

		
		String persistenceUnit = "jpa_mysql";
		EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnit);
		EntityManager em = emf.createEntityManager();
		
//		GenericDaoJPAPizzaRepository pizzaRepository = new GenericDaoJPAPizzaRepository();
//		Pizza read = pizzaRepository.read(1);
//		System.out.println("Read: " + read);
		
		Order order;
		Long orderId;
		Address address = new Address(null, "Ukraine", "Kyiv", "Velyka", "18", "Stan", "1234");
		Customer customer = new Customer();
		customer.setName("Vanya");
		customer.addAddress(address);
		OrderService orderService = appContext.getBean(OrderService.class);
		Integer[] pizzasId = new Integer[] { 1, 2, 3 };
		order = orderService.placeNewOrder(customer, address, pizzasId);
		orderId = order.getId();
		System.out.println("Order in java: " + order);
		System.out.println("Order in db: " + em.find(Order.class, orderId));
		order.setAddress(customer.getAddresses().iterator().next());
		orderService.processOrder(order);
		System.out.println("Processed order in java: " + order);
		System.out.println("Processed order in db: " + em.find(Order.class, orderId));
		orderService.doneOrder(order);
		System.out.println("Done order in java: " + order);
		System.out.println("Done order in db: " + em.find(Order.class, orderId));

		appContext.close();
	}
}
