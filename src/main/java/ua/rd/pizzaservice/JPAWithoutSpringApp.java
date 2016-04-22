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
import ua.rd.pizzaservice.domain.pizza.Pizza.PizzaType;
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

		ConfigurableApplicationContext repContext = new ClassPathXmlApplicationContext(
                "repositoryContext.xml");
        repContext.getEnvironment().setActiveProfiles("dev");
        repContext.refresh();
        ConfigurableApplicationContext appContext = new ClassPathXmlApplicationContext(
                new String[]{"appContext.xml"}, repContext);
        Order order;
        Customer customerOrder = new Customer();
        customerOrder.setAddress(address);
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
