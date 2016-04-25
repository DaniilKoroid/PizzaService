package ua.rd.pizzaservice;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ua.rd.pizzaservice.domain.customer.Customer;
import ua.rd.pizzaservice.domain.order.Order;
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
		
		
		
		Order order;
		Customer customer = em.find(Customer.class, 1);
		OrderService orderService = appContext.getBean(OrderService.class);
		Integer[] pizzasId = new Integer[] { 1, 2, 3 };
		order = orderService.placeNewOrder(customer, pizzasId);
		System.out.println("Order: " + order);

		try {
			em.persist(order);
		} catch (Throwable e) {
			System.out.println(e.toString());
		} finally {
			em.close();
			emf.close();
		}

		System.out.println(order);

		appContext.close();
	}
}
