package ua.rd.pizzaservice;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import ua.rd.pizzaservice.domain.accumulationcard.AccumulationCard;
import ua.rd.pizzaservice.domain.address.Address;
import ua.rd.pizzaservice.domain.customer.Customer;
import ua.rd.pizzaservice.domain.pizza.Pizza;
import ua.rd.pizzaservice.domain.pizza.Pizza.PizzaType;

public class JPAWithoutSpringPizzaApp {

	public static void main(String[] args) {
		persistActivatedAccumulationCard();
		persistNotActivatedAccumulationCard();
	}
	
	private static void persistPizza() {
		Pizza pizza = createPizza();
		testPersisting(pizza);
	}
	
	private static void persistAddress() {
		Address address = createAddressOne();
		testPersisting(address);
	}

	private static void persistCustomer() {
		Customer customer = createCustomer();
		testPersisting(customer);
	}
	
	private static void persistActivatedAccumulationCard() {
		AccumulationCard card = createActivatedAccumulationCard();
		testPersisting(card);
	}
	
	private static void persistNotActivatedAccumulationCard() {
		AccumulationCard card = createNotActivatedAccumulationCard();
		testPersisting(card);
	}
	
	private static Address createAddressOne() {
		Address address = new Address();
		address.setZipCode("zipCode12345");
		address.setCountry("Ukraine");
		address.setCity("Kyiv");
		address.setStreet("Lypova");
		address.setBuilding("321");
		address.setFlatNumber("23");
		return address;
	}
	
	private static Address createAddressTwo() {
		Address address = new Address();
		address.setZipCode("zipCode1533");
		address.setCountry("Ukraine");
		address.setCity("Kyiv");
		address.setStreet("Medova");
		address.setBuilding("12");
		address.setFlatNumber("14");
		return address;
	}
	
	private static Customer createCustomer() {
		Customer customer = new Customer();
		customer.setName("Ivan");
		customer.addAddress(createAddressOne());
		customer.addAddress(createAddressTwo());
		return customer;
	}
	
	private static Pizza createPizza() {
		Pizza pizza = new Pizza();
		pizza.setName("Margarita");
		pizza.setPrice(60.0d);
		pizza.setType(PizzaType.VEGETERIAN);
		return pizza;
	}
	
	private static AccumulationCard createActivatedAccumulationCard() {
		AccumulationCard card = new AccumulationCard();
		card.setAmount(111.1d);
		card.setIsActivated(true);
		return card;
	}
	
	private static AccumulationCard createNotActivatedAccumulationCard() {
		AccumulationCard card = new AccumulationCard();
		card.setAmount(0d);
		card.setIsActivated(false);
		return card;
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
