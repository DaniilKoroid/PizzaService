package ua.rd.pizzaservice;

import java.util.Arrays;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ua.rd.pizzaservice.domain.address.Address;
import ua.rd.pizzaservice.domain.customer.Customer;
import ua.rd.pizzaservice.domain.order.Order;
import ua.rd.pizzaservice.service.order.OrderService;

public class JPAWithoutSpringApp {

	public static void main(String[] args) {

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa_mysql");
		EntityManager em = emf.createEntityManager();
//		Pizza pizza = new Pizza();
		// pizza.setId(3);
//		pizza.setName("Seazza");
//		pizza.setPrice(111.0d);
//		pizza.setType(PizzaType.SEA);

		Address address = new Address();
//		address.setId(1);
		address.setZipCode("zipCode");
		address.setCountry("Ukraine");
		address.setCity("Kyiv");
		address.setStreet("K");
		address.setBuilding("18");
		address.setFlatNumber("Stanford");
//		address.setState(new State("state_column"));

		Customer customer = new Customer();
		customer.setName("empty");
		customer.setAddress(Arrays.asList(address, address));
		customer.setPhones(Arrays.asList("123", "456"));
		address.setCustomer(customer);
		
		try {
			em.getTransaction().begin();
			em.persist(customer);
			em.getTransaction().commit();
			em.clear();
//			Customer customer2 = address.getCustomer();
//			System.out.println("Address's customer: " + customer2);
//			for(Address adr : customer.getAddress()) {
//				System.out.println("Adr: " + em.find(Address.class, adr.getId()));
//			}
//			Address address2 = em.find(Address.class, customer.getAddress().get(0).getId());
//			System.out.println(address2.getCustomer());
//			Customer customer1 = em.find(Customer.class, customer.getId());
//			System.out.println(customer1);
		} finally {
			em.close();
			emf.close();
		}
		
		
		if (true) {
			return;
		}
		
		ConfigurableApplicationContext repContext = new ClassPathXmlApplicationContext(
                "repositoryContext.xml");
        repContext.getEnvironment().setActiveProfiles("dev");
        repContext.refresh();
        ConfigurableApplicationContext appContext = new ClassPathXmlApplicationContext(
                new String[]{"appContext.xml"}, repContext);
        Order order;
        Customer customerOrder = new Customer();
//        customerOrder.setAddress(address);
        customerOrder.setName("CustomerName");
        OrderService orderService = appContext.getBean(OrderService.class);
        Integer[] pizzasId = new Integer[]{1, 2, 3};
        order = orderService.placeNewOrder(customerOrder, pizzasId);
		
        try {
        	em.getTransaction().begin();
        	em.persist(order);
        	em.getTransaction().commit();
        } finally {
        	em.close();
        	emf.close();
        }
        
		
//		Customer customer = new Customer();
////		customer.setId(1);
//		customer.setName("Username");
//		customer.setAddress(address);
////		customer.setPhones(Arrays.asList("2qwer", "drftgyhji", "dfghjk"));
//		try {
//			em.getTransaction().begin();
//			em.persist(pizza);
//			em.getTransaction().commit();
//
//			em.getTransaction().begin();
//			em.persist(customer);
//			em.getTransaction().commit();
//
//		} finally {
//			em.close();
//			emf.close();
//		}

	}

}
