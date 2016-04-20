package ua.rd.pizzaservice;

import java.util.Arrays;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import ua.rd.pizzaservice.domain.address.Address;
import ua.rd.pizzaservice.domain.customer.Customer;
import ua.rd.pizzaservice.domain.pizza.Pizza;
import ua.rd.pizzaservice.domain.pizza.Pizza.PizzaType;

public class JPAWithoutSpringApp {

	public static void main(String[] args) {

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa_mysql");
		EntityManager em = emf.createEntityManager();
		Pizza pizza = new Pizza();
		// pizza.setId(3);
		pizza.setName("Seazza");
		pizza.setPrice(111.0d);
		pizza.setType(PizzaType.SEA);

		Address address = new Address();
		address.setId(1);
		address.setCity("Kyiv");

		Customer customer = new Customer();
//		customer.setId(1);
		customer.setName("Username");
		customer.setAddress(Arrays.asList(address, address));
		customer.setPhones(Arrays.asList("2qwer", "drftgyhji", "dfghjk"));
		try {
			em.getTransaction().begin();
			em.persist(pizza);
			em.getTransaction().commit();

			em.getTransaction().begin();
			em.persist(customer);
			em.getTransaction().commit();

//			Pizza pzza = em.find(Pizza.class, 2);
//			Customer cust = em.find(Customer.class, 1);
//			System.out.println(pzza);
		} finally {
			em.close();
			emf.close();
		}

	}

}
