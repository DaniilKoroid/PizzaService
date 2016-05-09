package ua.rd.pizzaservice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
			testPizzaRepository(pizzaRepository);
			testAddressRepository(appContext.getBean(AddressRepository.class));
			testCustomerRepository(appContext.getBean(CustomerRepository.class));
			testAccumulationCardRepository(appContext.getBean(AccumulationCardRepository.class));
			testOrderRepository(appContext.getBean(OrderRepository.class), pizzaRepository);
		} catch (Throwable t) {
			while (t != null) {
				System.out.println("-> " + t.getLocalizedMessage());
				t.printStackTrace();
				t = t.getCause();
			}
		} finally {
			repContext.close();
			appContext.close();
		}
	}

	private static void testPizzaRepository(PizzaRepository pizzaRepository) {
		PizzaRepository pizzaRep = pizzaRepository;

		Pizza pizza = new Pizza();
		pizza.setName("Allegro");
		pizza.setPrice(142.2d);
		pizza.setType(PizzaType.MEAT);
		System.out.println("Pizza before persisting: " + pizza);
		Pizza createdPizza = pizzaRep.create(pizza);
		Integer createdPizzaId = createdPizza.getId();
		System.out.println("Created pizza: " + createdPizza);

		Pizza pizzaByID = pizzaRep.getPizzaByID(createdPizzaId);
		System.out.println("Found created pizza by rep: " + pizzaByID);

		pizza.setName("New pizza name123777");
		System.out.println("Pizza before update: " + pizza);
		Pizza updatedPizza = pizzaRep.update(pizza);
		System.out.println("Updated pizza by repository: " + updatedPizza);

		pizzaRep.delete(pizza);
		System.out.println("Pizza after deleting by repository: " + pizza);
	}

	private static void testAddressRepository(AddressRepository ar) {
		AddressRepository addrRep = ar;

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

		Address addrByID = addrRep.read(createdAddressId);
		System.out.println("Found created address by addrRep: " + addrByID);

		addr.setCity("Kharkiv");
		System.out.println("Address before update: " + addr);
		Address updatedAddress = addrRep.update(addr);
		System.out.println("Updated address by repository: " + updatedAddress);

		addrRep.delete(addr);
		System.out.println("Address after deleting by repository: " + addr);
	}

	private static void testCustomerRepository(CustomerRepository cr) {
		String entityName = "customer";
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

		Customer customerByID = custRep.read(createdCustomerId);
		System.out.println("Found created " + entityName + " by customerRep: " + customerByID);

		customer.setName("Newvan");
		System.out.println(entityName + " before update: " + customer);
		Customer updatedCustomer = custRep.update(customer);
		System.out.println("Updated " + entityName + " by repository: " + updatedCustomer);

		custRep.delete(customer);
		System.out.println(entityName + " after deleting by repository: " + customer);
	}

	private static void testAccumulationCardRepository(AccumulationCardRepository acr) {
		String entityName = "accumulation card";
		AccumulationCardRepository cardRep = acr;

		Customer customer = new Customer();
		customer.setName("Ivanovka");

		AccumulationCard card = new AccumulationCard();
		card.setAmount(100.0d);
		card.setIsActivated(true);
		card.setOwner(customer);
		System.out.println(entityName + " before persisting: " + card);
		AccumulationCard createdCard = cardRep.create(card);
		Integer createdAccumulationCardId = createdCard.getId();
		System.out.println("Created " + entityName + ": " + createdCard);

		AccumulationCard accumulationCardByID = cardRep.read(createdAccumulationCardId);
//		Customer customer2 = accumulationCardByID.getOwner();
//		Set<Address> custAdr = customer2.getAddresses();
		
		
		System.out.println("Found created " + entityName + " by rep: " + accumulationCardByID);

		card.setAmount(1789d);
		System.out.println(entityName + " before update: " + card);
		AccumulationCard updatedAccumulationCard = cardRep.update(card);
		System.out.println("Updated " + entityName + " by repository: " + updatedAccumulationCard);

		Optional<Customer> owner = cardRep.getOwner(card);
		System.out.println("Owner by rep: " + owner);

		cardRep.delete(card);
		System.out.println(entityName + " after deleting by repository: " + card);
	}

	private static void testOrderRepository(OrderRepository or, PizzaRepository pr) {
		String entityName = "order";
		OrderRepository orderRep = or;

		Address addr = new Address();
		addr.setCountry("Ukraine");
		addr.setCity("Kyiv");
		addr.setStreet("Polyova");
		addr.setBuilding("14");
		addr.setFlatNumber("5");
		addr.setZipCode("1000");

//		Pizza pizza1 = new Pizza(null, "pizza1", 101.0d, PizzaType.MEAT);
//		Pizza pizza2 = new Pizza(null, "pizza2", 102.0d, PizzaType.MEAT);
//		Pizza pizza3 = new Pizza(null, "pizza3", 103.0d, PizzaType.MEAT);
		Pizza pizza1 = pr.getPizzaByID(2);
		Pizza pizza2 = pr.getPizzaByID(3);
		Pizza pizza3 = pr.getPizzaByID(4);

		Customer customer = new Customer();
		customer.setName("Ivan");
		customer.addAddress(addr);

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

		Order createdOrder = orderRep.update(order);
		Long createdOrderId = createdOrder.getId();
		System.out.println("Created " + entityName + ": " + createdOrder);

		Order orderByID = orderRep.read(createdOrderId);
		System.out.println("Found created " + entityName + " by rep: " + orderByID);

		order.setState(OrderState.IN_PROGRESS);
		System.out.println(entityName + " before update: " + order);
		Order updatedOrder = orderRep.update(order);
		System.out.println("Updated " + entityName + " by repository: " + updatedOrder);

		orderRep.delete(order);
		System.out.println(entityName + " after deleting by repository: " + order);
	}

}
