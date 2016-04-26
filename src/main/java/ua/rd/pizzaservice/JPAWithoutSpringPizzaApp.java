package ua.rd.pizzaservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import ua.rd.pizzaservice.domain.accumulationcard.AccumulationCard;
import ua.rd.pizzaservice.domain.address.Address;
import ua.rd.pizzaservice.domain.customer.Customer;
import ua.rd.pizzaservice.domain.order.Order;
import ua.rd.pizzaservice.domain.order.OrderState;
import ua.rd.pizzaservice.domain.pizza.Pizza;
import ua.rd.pizzaservice.domain.pizza.Pizza.PizzaType;

public class JPAWithoutSpringPizzaApp {

	public static void main(String[] args) {
//		persistPizzaMargarita();
//		persistPizzaPepperoni();
//		persistAddress();
//		persistCustomer();
//		persistActivatedAccumulationCard();
//		persistNotActivatedAccumulationCard();
//		persistOrder();
//		persistPizzas();
//		persistAddresses();
//		persistCustomers();
	}

	private static void persistCustomers() {
		String persistenceUnitName = "jpa_mysql";
		EntityManagerFactory emf = null;
		EntityManager em = null;

		Customer customerOne = createCustomerOne();
		Customer customerTwo = createCustomerTwo();

		try {
			emf = Persistence.createEntityManagerFactory(persistenceUnitName);
			em = emf.createEntityManager();
			Address addr1 = em.find(Address.class, 1);
			Address addr2 = em.find(Address.class, 2);
			System.out.println("1: " + addr1);
			System.out.println("2: " + addr2);
			customerOne.setAddresses(new HashSet<Address>() {
				{
					add(addr1);
					add(addr2);
				}
			});
			// System.out.println(customerOne.getAddresses());
			customerTwo.setAddresses(new HashSet<Address>() {
				{
					add(addr2);
				}
			});
			persistObject(em, customerOne);
			persistObject(em, customerTwo);
		} finally {
			if (em != null) {
				em.close();
			}
			if (emf != null) {
				emf.close();
			}
		}
	}

	private static void persistPizzas() {
		List<Pizza> pizzas = new ArrayList<>();
		pizzas.add(new Pizza(1, "Margarita", 60d, PizzaType.MEAT));
		pizzas.add(new Pizza(2, "SeaPizza", 90d, PizzaType.SEA));
		pizzas.add(new Pizza(3, "Ayurveda", 80d, PizzaType.VEGETERIAN));

		for (Pizza pizza : pizzas) {
			pizza.setId(null);
			testPersisting(pizza);
		}
	}

	private static void persistAddresses() {
		testPersisting(createAddressOne());
		testPersisting(createAddressTwo());
	}

	private static void persistPizzaMargarita() {
		Pizza pizza = createPizzaMargarita();
		testPersisting(pizza);
	}

	private static void persistPizzaPepperoni() {
		Pizza pizza = createPizzaPepperoni();
		testPersisting(pizza);
	}

	private static void persistAddress() {
		Address address = createAddressOne();
		testPersisting(address);
	}

	private static void persistCustomer() {
		Customer customer = createCustomerOne();
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

	private static void persistOrder() {
		Order order = createOrder();
		testPersisting(order);
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

	private static Customer createCustomerOne() {
		Customer customer = new Customer();
		customer.setName("Ivan");
		customer.addAddress(createAddressOne());
		customer.addAddress(createAddressTwo());
		return customer;
	}

	private static Customer createCustomerTwo() {
		Customer customer = new Customer();
		customer.setName("Vasyl");
		customer.addAddress(createAddressTwo());
		return customer;
	}

	private static Pizza createPizzaMargarita() {
		Pizza pizza = new Pizza();
		pizza.setName("Margarita");
		pizza.setPrice(60.0d);
		pizza.setType(PizzaType.VEGETERIAN);
		return pizza;
	}

	private static Pizza createPizzaPepperoni() {
		Pizza pizza = new Pizza();
		pizza.setName("Pepperoni");
		pizza.setPrice(75.0d);
		pizza.setType(PizzaType.MEAT);
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

	private static Order createOrder() {
		Order order = new Order();
		order.setCustomer(createCustomerOne());
		order.setState(OrderState.IN_PROGRESS);
		Map<Pizza, Integer> pizzaMap = new HashMap<Pizza, Integer>() {
			private static final long serialVersionUID = -4235002947737145084L;
			{
				put(createPizzaMargarita(), Integer.valueOf(3));
				put(createPizzaPepperoni(), Integer.valueOf(2));
			}
		};
		order.setPizzas(pizzaMap);
		return order;
	}

	private static void testPersisting(Object obj) {
		String persistenceUnitName = "jpa_mysql";
		EntityManagerFactory emf = null;
		EntityManager em = null;

		try {
			emf = Persistence.createEntityManagerFactory(persistenceUnitName);
			em = emf.createEntityManager();
			persistObject(em, obj);
		} catch (Throwable e) {
			while (e != null) {
				System.out.println("Exception: " + e.getClass().getName());
				System.out.println("Msg: " + e.getLocalizedMessage());
				System.out.print("Stacktrace: ");
				for (StackTraceElement el : e.getStackTrace()) {
					System.out.println("-> " + el);
				}
				if (e.getCause() != null) {
					System.out.println("Caused by: " + e.getCause().getClass().getName());
					e = e.getCause();
				} else {
					e = null;
				}
			}
		} finally {
			if (em != null) {
				em.close();
			}
			if (emf != null) {
				emf.close();
			}
		}
	}

	private static void persistObject(EntityManager em, Object obj) {
		try {
			em.getTransaction().begin();
			// if (obj instanceof Order) {
			// Order order = (Order) obj;
			// for (Pizza pizza : order.getPizzas().keySet()) {
			// em.persist(pizza);
			// }
			// em.persist(order);
			// } else {
			// em.persist(obj);
			// }
			em.persist(obj);
			em.getTransaction().commit();
		} catch (Throwable e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw new RuntimeException(e);
		}
	}

}
