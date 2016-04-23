package ua.rd.pizzaservice;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import ua.rd.pizzaservice.domain.address.Address;
import ua.rd.pizzaservice.domain.pizza.Pizza;
import ua.rd.pizzaservice.domain.pizza.Pizza.PizzaType;

public class JPAWithoutSpringPizzaApp {

	public static void main(String[] args) {
		Pizza pizza = createPizza();
		Address address = createAddress();
		testPersisting(pizza);
		testPersisting(address);
	}

	private static Address createAddress() {
		Address address = new Address();
		address.setZipCode("zipCode12345");
		address.setCountry("Ukraine");
		address.setCity("Kyiv");
		address.setStreet("Lypova");
		address.setBuilding("321");
		address.setFlatNumber("23");
		return address;
	}
	
	private static Pizza createPizza() {
		Pizza pizza = new Pizza();
		pizza.setName("Margarita");
		pizza.setPrice(60.0d);
		pizza.setType(PizzaType.VEGETERIAN);
		return pizza;
	}
	
	private static void testPersisting(Object obj) {
		String persistenceUnitName = "jpa_mysql";
		EntityManagerFactory emf = null;
		EntityManager em = null;
		
		try {
			emf = Persistence.createEntityManagerFactory(persistenceUnitName);
			em = emf.createEntityManager();
			persistObject(em, obj);
		} finally {
			em.close();
			emf.close();
		}
	}
	
	private static void persistObject(EntityManager em, Object obj) {
		em.getTransaction().begin();
		em.persist(obj);
		em.getTransaction().commit();
	}

}
