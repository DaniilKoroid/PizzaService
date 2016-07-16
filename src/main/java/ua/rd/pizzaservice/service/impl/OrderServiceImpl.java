package ua.rd.pizzaservice.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;

import ua.rd.pizzaservice.domain.AccumulationCard;
import ua.rd.pizzaservice.domain.Address;
import ua.rd.pizzaservice.domain.Customer;
import ua.rd.pizzaservice.domain.Order;
import ua.rd.pizzaservice.domain.OrderState;
import ua.rd.pizzaservice.domain.Pizza;
import ua.rd.pizzaservice.repository.OrderRepository;
import ua.rd.pizzaservice.service.AccumulationCardService;
import ua.rd.pizzaservice.service.CustomerService;
import ua.rd.pizzaservice.service.DiscountService;
import ua.rd.pizzaservice.service.OrderService;
import ua.rd.pizzaservice.service.PizzaService;

@Service
public class OrderServiceImpl implements OrderService {

	//TODO: tests for repos methods
	
	private static final int MIN_PIZZA_IN_ORDER_COUNT = 1;
	private static final int MAX_PIZZA_IN_ORDER_COUNT = 10;

	@Autowired
	private DiscountService discountService;
	
	@Autowired
	private AccumulationCardService accCardService;
	
	@Autowired
	private PizzaService pizzaService;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private CustomerService customerService;

	OrderServiceImpl() {
	}

	public OrderServiceImpl(DiscountService discountService, AccumulationCardService accCardService,
			PizzaService pizzaService, OrderRepository orderRepository, CustomerService customerService) {
		this.discountService = discountService;
		this.accCardService = accCardService;
		this.pizzaService = pizzaService;
		this.orderRepository = orderRepository;
		this.customerService = customerService;
	}

	@Override
	public Order placeNewOrder(Customer customer, Address deliveryAddress, Integer... pizzasID) {
		checkOrderedPizzasNumber(pizzasID);
		checkCustomerExistance(customer);
		checkAddressExistance(deliveryAddress);

		Map<Pizza, Integer> pizzas = pizzasByArrOfId(pizzasID);
		Order newOrder = createOrder(customer, pizzas, deliveryAddress);

		orderRepository.saveOrder(newOrder);
		return newOrder;
	}
	
	@Override
	public Order placeNewOrder(Customer customer, Address deliveryAddress, Map<Pizza, Integer> pizzas) {
		checkOrderedPizzasNumber(pizzas);
		checkCustomerExistance(customer);
		checkAddressExistance(deliveryAddress);
		
		Order newOrder = createOrder(customer, pizzas, deliveryAddress);

		orderRepository.saveOrder(newOrder);
		return newOrder;
	}
	
	private Order createOrder(Customer customer, Map<Pizza, Integer> pizzas, Address deliveryAddress) {
		Order newOrder = createOrder();
		newOrder.setState(OrderState.NEW);
		newOrder.setCustomer(customer);
		newOrder.setAddress(deliveryAddress);
		newOrder.setPizzas(pizzas);
		newOrder.setCreationDate(LocalDateTime.now());
		newOrder.setDeliveryDate(LocalDateTime.now().plusMinutes(30));
		customerService.proposeAddress(deliveryAddress, customer);
		return newOrder;
	}

	@Lookup(value = "order")
	protected Order createOrder() {
		return null;
	}

	private Map<Pizza, Integer> pizzasByArrOfId(Integer... pizzasID) {
		Map<Pizza, Integer> pizzas = new HashMap<>();
		int toAdd = 1;
		for (Integer id : pizzasID) {
			Pizza pizza = pizzaService.getPizzaByID(id);

			if (pizzas.containsKey(pizza)) {
				Integer curValue = pizzas.get(pizza);
				curValue += toAdd;
				pizzas.put(pizza, curValue);
			} else {
				pizzas.put(pizza, toAdd);
			}
		}
		return pizzas;
	}

	private void checkOrderedPizzasNumber(Integer... pizzasId) {
		if (pizzasId.length < MIN_PIZZA_IN_ORDER_COUNT || pizzasId.length > MAX_PIZZA_IN_ORDER_COUNT) {

			throw new IllegalArgumentException("Can't place order with " + "not allowed number of pizzas.");
		}
	}
	
	private void checkOrderedPizzasNumber(Map<Pizza, Integer> pizzas) {
		int totalCount = 0;
		for (Integer pizzaCount : pizzas.values()) {
			totalCount += pizzaCount;
		}
		if (totalCount < MIN_PIZZA_IN_ORDER_COUNT || totalCount > MAX_PIZZA_IN_ORDER_COUNT) {

			throw new IllegalArgumentException("Can't place order with " + "not allowed number of pizzas.");
		}
	}

	private void checkCustomerExistance(Customer customer) {
		if (customer == null) {
			throw new IllegalArgumentException("Customer must exist to place new order");
		}
	}
	
	private void checkAddressExistance(Address address) {
		if (address == null) {
			throw new IllegalArgumentException("Address must exist to place new order");
		}
	}

	@Override
	public Boolean changeOrder(Order order, Integer... pizzasID) {
		checkOrderedPizzasNumber(pizzasID);
		Boolean canChange = canChange(order);
		if (canChange) {
			Map<Pizza, Integer> pizzas = pizzasByArrOfId(pizzasID);
			canChange = order.changeOrder(pizzas);
			orderRepository.update(order);
		}
		return canChange;
	}

	@Override
	public Boolean canChange(Order order) {
		Boolean canChange = order.canChange();
		return canChange;
	}

	@Override
	public Boolean processOrder(Order order) {
		Boolean result = Boolean.FALSE;
		Boolean canProceedToState = order.canProceedToState(OrderState.IN_PROGRESS);
		if (canProceedToState) {
			result = order.nextState();
			orderRepository.update(order);
		}
		return result;
	}

	@Override
	public Boolean cancelOrder(Order order) {
		Boolean isCancelled = order.cancel();
		if (isCancelled) {
			orderRepository.update(order);
		}
		return isCancelled;
	}

	@Override
	public Boolean doneOrder(Order order) {
		Boolean canProceedToState = order.canProceedToState(OrderState.DONE);
		Boolean result = Boolean.FALSE;
		if (canProceedToState) {
			result = order.nextState();
			processPayment(order);
			orderRepository.update(order);
		}
		return result;
	}

	private Boolean processPayment(Order order) {
		Customer customer = order.getCustomer();
		if (accCardService.hasAccumulationCard(customer)) {
			AccumulationCard card = accCardService.getAccumulationCardByCustomer(customer);
			Double priceWithDiscounts = discountService.calculatePriceWithDiscounts(order);
			accCardService.use(card, priceWithDiscounts);
		}
		return Boolean.TRUE;
	}

	@Override
	public Double getFullPrice(Order order) {
		return order.calculateFullPrice();
	}

	@Override
	public Double getDiscountAmount(Order order) {
		return discountService.calculateFinalDiscountAmount(order);
	}

	@Override
	public Double getFinalPrice(Order order) {
		Double finalPrice = getFullPrice(order) - getDiscountAmount(order);
		return finalPrice;
	}

	@Override
	public Order create(Order order) {
		return orderRepository.create(order);
	}

	@Override
	public Order findById(Long id) {
		return orderRepository.read(id);
	}

	@Override
	public List<Order> findAllOrders() {
		return orderRepository.findAllOrders();
	}

	@Override
	public Long saveOrder(Order newOrder) {
		return orderRepository.saveOrder(newOrder);
	}
}
