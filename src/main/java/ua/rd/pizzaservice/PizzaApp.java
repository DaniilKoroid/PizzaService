package ua.rd.pizzaservice;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ua.rd.pizzaservice.domain.address.Address;
import ua.rd.pizzaservice.domain.customer.Customer;
import ua.rd.pizzaservice.domain.order.Order;
import ua.rd.pizzaservice.domain.pizza.Pizza;
import ua.rd.pizzaservice.repository.pizza.GenericDaoJPAPizzaRepository;
import ua.rd.pizzaservice.service.order.OrderService;

public class PizzaApp {
	public static void main(String[] args) {

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
		Customer customer = new Customer();
		customer.setName("Vanya");
		customer.addAddress(new Address(null, "Ukraine", "Kyiv", "Velyka", "18", "Stan", "1234"));
		OrderService orderService = appContext.getBean(OrderService.class);
		Integer[] pizzasId = new Integer[] { 1, 2, 3 };
		order = orderService.placeNewOrder(customer, pizzasId);
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

//		try {
//			em.persist(order);
//			Order foundOrder = em.find(Order.class, order.getId());
//			System.out.println("Found order: " + foundOrder);
//		} catch (Throwable e) {
//			System.out.println(e.toString());
//		} finally {
//			em.close();
//			emf.close();
//		}

//		System.out.println(order);

		appContext.close();
	}
}
