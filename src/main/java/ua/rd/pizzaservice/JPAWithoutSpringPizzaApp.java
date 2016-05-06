package ua.rd.pizzaservice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ua.rd.pizzaservice.domain.accumulationcard.AccumulationCard;
import ua.rd.pizzaservice.domain.address.Address;
import ua.rd.pizzaservice.domain.customer.Customer;
import ua.rd.pizzaservice.domain.order.Order;
import ua.rd.pizzaservice.domain.order.OrderState;
import ua.rd.pizzaservice.domain.pizza.Pizza;
import ua.rd.pizzaservice.domain.pizza.Pizza.PizzaType;
import ua.rd.pizzaservice.repository.accumulationcard.AccumulationCardRepository;
import ua.rd.pizzaservice.repository.address.AddressRepository;
import ua.rd.pizzaservice.repository.customer.CustomerRepository;
import ua.rd.pizzaservice.repository.order.OrderRepository;
import ua.rd.pizzaservice.repository.pizza.PizzaRepository;

public class JPAWithoutSpringPizzaApp {

	public static void main(String[] args) {
		ConfigurableApplicationContext repContext = null;
		ConfigurableApplicationContext appContext = null;

		try {
			repContext = new ClassPathXmlApplicationContext("repositoryMySQLContext.xml");
			repContext.refresh();
			appContext = new ClassPathXmlApplicationContext(new String[] { "appContext.xml" }, repContext);
			PizzaRepository pizzaRepository = appContext.getBean(PizzaRepository.class);
			EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa_mysql");
			testPizzaRepository(emf, pizzaRepository);
			testAddressRepository(emf, appContext.getBean(AddressRepository.class));
			testCustomerRepository(emf, appContext.getBean(CustomerRepository.class));
			testAccumulationCardRepository(emf, appContext.getBean(AccumulationCardRepository.class));
			testOrderRepository(emf, appContext.getBean(OrderRepository.class));
			emf.close();
		} finally {
			repContext.close();
			appContext.close();
		}
	}

	private static void testPizzaRepository(EntityManagerFactory emf, PizzaRepository pizzaRepository) {
		PizzaRepository pizzaRep = pizzaRepository;

		Pizza pizza = new Pizza();
		pizza.setName("Allegro");
		pizza.setPrice(142.2d);
		pizza.setType(PizzaType.MEAT);
		System.out.println("Pizza before persisting: " + pizza);
		Pizza createdPizza = pizzaRep.create(pizza);
		Integer createdPizzaId = createdPizza.getId();
		System.out.println("Created pizza: " + createdPizza);

		EntityManager em = null;
		em = emf.createEntityManager();
		Pizza findCreatedPizza = em.find(Pizza.class, createdPizzaId);
		System.out.println("Found created pizza by EntityManager: " + findCreatedPizza);
		em.close();

		Pizza pizzaByID = pizzaRep.getPizzaByID(createdPizzaId);
		System.out.println("Found created pizza by rep: " + pizzaByID);

		pizza.setName("New pizza name123777");
		System.out.println("Pizza before update: " + pizza);
		Pizza updatedPizza = pizzaRep.update(pizza);
		System.out.println("Updated pizza by repository: " + updatedPizza);

		em = emf.createEntityManager();
		Pizza findUpdatedPizza = em.find(Pizza.class, createdPizzaId);
		System.out.println("Found updated pizza by EntityManager: " + findUpdatedPizza);
		em.close();

		pizzaRep.delete(pizza);
		System.out.println("Pizza after deleting by repository: " + pizza);
		em = emf.createEntityManager();
		Pizza findDeletedPizza = em.find(Pizza.class, createdPizzaId);
		System.out.println("Found deleted pizza by EntityManager: " + findDeletedPizza);
		em.close();
	}

	private static void testAddressRepository(EntityManagerFactory emf, AddressRepository ar) {
		AddressRepository addrRep = ar;
		// AddressRepository addrRep = new GenericDaoJPAAddressRepository();

		Address addr = new Address();
		addr.setCountry("Ukraine");
		addr.setCity("Kyiv");
		addr.setStreet("Polyova");
		addr.setBuilding("14");
		addr.setFlatNumber("5");
		addr.setZipCode("1000");

		System.out.println("Address before persisting: " + addr);
		Address createdAddr = addrRep.create(addr);
		Integer createdAddressId = createdAddr.getId();
		System.out.println("Created address: " + createdAddr);

		EntityManager em = null;
		em = emf.createEntityManager();
		Address findCreatedAddress = em.find(Address.class, createdAddressId);
		System.out.println("Found created address by EntityManager: " + findCreatedAddress);
		em.close();

		Address addrByID = addrRep.read(createdAddressId);
		System.out.println("Found created address by addrRep: " + addrByID);

		addr.setCity("Kharkiv");
		System.out.println("Address before update: " + addr);
		Address updatedAddress = addrRep.update(addr);
		System.out.println("Updated address by repository: " + updatedAddress);

		em = emf.createEntityManager();
		Address findUpdatedAddress = em.find(Address.class, createdAddressId);
		System.out.println("Found updated address by EntityManager: " + findUpdatedAddress);
		em.close();

		addrRep.delete(addr);
		System.out.println("Address after deleting by repository: " + addr);
		em = emf.createEntityManager();
		Address findDeletedAddress = em.find(Address.class, createdAddressId);
		System.out.println("Found deleted address by EntityManager: " + findDeletedAddress);
		em.close();
	}

	private static void testCustomerRepository(EntityManagerFactory emf, CustomerRepository cr) {
		Class<Customer> clazz = Customer.class;
		String entityName = "customer";
//		CustomerRepository custRep = new GenericDaoJPACustomerRepository();
		CustomerRepository custRep = cr;

		Customer customer = new Customer();
		customer.setName("Ivan");
		Address address = new Address();
		address.setCountry("Ukraine");
		customer.addAddress(address);
		System.out.println(entityName + " before persisting: " + customer);
		Customer createdCust = custRep.create(customer);
		Integer createdCustomerId = createdCust.getId();
		System.out.println("Created " + entityName + ": " + createdCust);

		EntityManager em = null;
		em = emf.createEntityManager();
		Customer findCreatedCustomer = em.find(clazz, createdCustomerId);
		System.out.println("Found created " + entityName + " by EntityManager: " + findCreatedCustomer);
		em.close();

		Customer customerByID = custRep.read(createdCustomerId);
		System.out.println("Found created " + entityName + " by customerRep: " + customerByID);

		customer.setName("Newvan");
		System.out.println(entityName + " before update: " + customer);
		Customer updatedCustomer = custRep.update(customer);
		System.out.println("Updated " + entityName + " by repository: " + updatedCustomer);

		em = emf.createEntityManager();
		Customer findUpdatedCustomer = em.find(clazz, createdCustomerId);
		System.out.println("Found updated " + entityName + " by EntityManager: " + findUpdatedCustomer);
		em.close();

		custRep.delete(customer);
		System.out.println(entityName + " after deleting by repository: " + customer);
		em = emf.createEntityManager();
		Customer findDeletedCustomer = em.find(clazz, createdCustomerId);
		System.out.println("Found deleted " + entityName + " by EntityManager: " + findDeletedCustomer);
		em.close();
	}

	private static void testAccumulationCardRepository(EntityManagerFactory emf, AccumulationCardRepository acr) {
		Class<AccumulationCard> clazz = AccumulationCard.class;
		String entityName = "accumulation card";
//		AccumulationCardRepository cardRep = new GenericDaoJPAAccumulationCardRepository();
		AccumulationCardRepository cardRep = acr;

		EntityManager entityManager = emf.createEntityManager();
		Customer customer = new Customer();
		customer.setName("Ivanovka");
		entityManager.getTransaction().begin();
		entityManager.persist(customer);
		entityManager.getTransaction().commit();
		System.out.println("Persisted customer: " + customer);
		customer = entityManager.find(Customer.class, customer.getId());
		System.out.println("Found customer: " + customer);
		entityManager.close();

		AccumulationCard card = new AccumulationCard();
		card.setAmount(100.0d);
		card.setIsActivated(true);
		card.setOwner(customer);
		System.out.println(entityName + " before persisting: " + card);
		AccumulationCard createdCard = cardRep.create(card);
		Integer createdAccumulationCardId = createdCard.getId();
		System.out.println("Created " + entityName + ": " + createdCard);

		EntityManager em = null;
		em = emf.createEntityManager();
		AccumulationCard findCreatedAccumulationCard = em.find(clazz, createdAccumulationCardId);
		System.out.println("Found created " + entityName + " by EntityManager: " + findCreatedAccumulationCard);
		em.close();

		AccumulationCard accumulationCardByID = cardRep.read(createdAccumulationCardId);
		System.out.println("Found created " + entityName + " by rep: " + accumulationCardByID);

		card.setAmount(1789d);
		System.out.println(entityName + " before update: " + card);
		AccumulationCard updatedAccumulationCard = cardRep.update(card);
		System.out.println("Updated " + entityName + " by repository: " + updatedAccumulationCard);

		em = emf.createEntityManager();
		AccumulationCard findUpdatedAccumulationCard = em.find(clazz, createdAccumulationCardId);
		System.out.println("Found updated " + entityName + " by EntityManager: " + findUpdatedAccumulationCard);
		em.close();

		Optional<Customer> owner = cardRep.getOwner(card);
		System.out.println("Owner by rep: " + owner);

		cardRep.delete(card);
		System.out.println(entityName + " after deleting by repository: " + card);
		em = emf.createEntityManager();
		AccumulationCard findDeletedAccumulationCard = em.find(clazz, createdAccumulationCardId);
		System.out.println("Found deleted " + entityName + " by EntityManager: " + findDeletedAccumulationCard);
		em.close();
	}

	private static void testOrderRepository(EntityManagerFactory emf, OrderRepository or) {
		Class<Order> clazz = Order.class;
		String entityName = "order";
//		OrderRepository orderRep = new GenericDaoJPAOrderRepository();
		OrderRepository orderRep = or;

		EntityManager entityManager = emf.createEntityManager();

		Address addr = new Address();
		addr.setCountry("Ukraine");
		addr.setCity("Kyiv");
		addr.setStreet("Polyova");
		addr.setBuilding("14");
		addr.setFlatNumber("5");
		addr.setZipCode("1000");

		Pizza pizza1 = new Pizza(null, "pizza1", 101.0d, PizzaType.MEAT);
		Pizza pizza2 = new Pizza(null, "pizza2", 102.0d, PizzaType.MEAT);
		Pizza pizza3 = new Pizza(null, "pizza3", 103.0d, PizzaType.MEAT);

		entityManager.getTransaction().begin();
		entityManager.persist(addr);
		entityManager.persist(pizza1);
		entityManager.persist(pizza2);
		entityManager.persist(pizza3);
		entityManager.getTransaction().commit();

//		addr = entityManager.find(Address.class, addr.getId());
		Customer customer = new Customer();
		customer.setName("Ivan");
		customer.addAddress(addr);

		entityManager.getTransaction().begin();
		entityManager.persist(customer);
		entityManager.getTransaction().commit();

//		pizza1 = entityManager.find(Pizza.class, pizza1.getId());
//		pizza2 = entityManager.find(Pizza.class, pizza2.getId());
//		pizza3 = entityManager.find(Pizza.class, pizza3.getId());
//		customer = entityManager.find(Customer.class, customer.getId());

		entityManager.close();

		Map<Pizza, Integer> pizzas = new HashMap<>();
		pizzas.put(pizza1, 1);
		pizzas.put(pizza2, 2);
		pizzas.put(pizza3, 5);

		Order order = new Order();
		order.setAddress(addr);
		order.setCreationDate(LocalDateTime.now());
		order.setDeliveryDate(LocalDateTime.now().plusMinutes(30));
		order.setCustomer(customer);
		order.setPizzas(pizzas);
		order.setState(OrderState.NEW);

		System.out.println(entityName + " before persisting: " + order);

//		Order createdOrder = orderRep.create(order);
		Order createdOrder = orderRep.update(order);
		Long createdOrderId = createdOrder.getId();
		System.out.println("Created " + entityName + ": " + createdOrder);

		EntityManager em = null;
		em = emf.createEntityManager();
		Order findCreatedOrder = em.find(clazz, createdOrderId);
		System.out.println("Found created " + entityName + " by EntityManager: " + findCreatedOrder);
		em.close();

		Order orderByID = orderRep.read(createdOrderId);
		System.out.println("Found created " + entityName + " by rep: " + orderByID);

		order.setState(OrderState.IN_PROGRESS);
		System.out.println(entityName + " before update: " + order);
		Order updatedOrder = orderRep.update(findCreatedOrder);
		System.out.println("Updated " + entityName + " by repository: " + updatedOrder);

		em = emf.createEntityManager();
		createdOrderId = updatedOrder.getId();
		Order findUpdatedOrder = em.find(clazz, createdOrderId);
		System.out.println("Found updated " + entityName + " by EntityManager: " + findUpdatedOrder);
		em.close();

		orderRep.delete(order);
		System.out.println(entityName + " after deleting by repository: " + order);
		em = emf.createEntityManager();
		Order findDeletedOrder = em.find(clazz, createdOrderId);
		System.out.println("Found deleted " + entityName + " by EntityManager: " + findDeletedOrder);
		em.close();
	}

}
