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
import ua.rd.pizzaservice.service.OrderPriceCalculatorService;
import ua.rd.pizzaservice.service.OrderService;
import ua.rd.pizzaservice.service.PizzaService;

@Service
public class OrderServiceImpl implements OrderService {
	
	private static final int MIN_PIZZA_IN_ORDER_COUNT = 1;
	private static final int MAX_PIZZA_IN_ORDER_COUNT = 10;

	private DiscountService discountService;
	
	private AccumulationCardService accCardService;
	
	private PizzaService pizzaService;
	
	private OrderRepository orderRepository;
	
	private CustomerService customerService;
	
	private OrderPriceCalculatorService orderPriceCalculatorService;
	
	@Autowired
	public OrderServiceImpl(DiscountService discountService,
							AccumulationCardService accCardService,
							PizzaService pizzaService,
							OrderRepository orderRepository,
							CustomerService customerService,
							OrderPriceCalculatorService orderPriceCalculatorService) {
		this.discountService = discountService;
		this.accCardService = accCardService;
		this.pizzaService = pizzaService;
		this.orderRepository = orderRepository;
		this.customerService = customerService;
		this.orderPriceCalculatorService = orderPriceCalculatorService;
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

	@Override
	public Boolean changeOrder(Order order, Integer... pizzasID) {
		checkOrderedPizzasNumber(pizzasID);
		Boolean canChange = canChange(order);
		if (canChange) {
			Map<Pizza, Integer> pizzas = pizzasByArrOfId(pizzasID);
			setNewPizzasToOrder(order, pizzas);
			orderRepository.update(order);
		}
		return canChange;
	}
	
	private void setNewPizzasToOrder(Order order, Map<Pizza, Integer> newPizzas) {
		order.setPizzas(newPizzas);
	}

	@Override
	public Boolean processOrder(Order order) {
		Boolean result = Boolean.FALSE;
		Boolean canProceedToState = canOrderProceedToState(order, OrderState.IN_PROGRESS);
		if (canProceedToState) {
			result = proceedOrderToNextState(order);
			orderRepository.update(order);
		}
		return result;
	}

	@Override
	public Boolean cancelOrder(Order order) {
		Boolean canProceedToCancelled = canOrderProceedToState(order, OrderState.CANCELLED);
		boolean canceled = false;
		if (canProceedToCancelled) {
			canceled = proceedOrderToCancelledState(order);
			orderRepository.update(order);
		}
		return canceled;
	}

	@Override
	public Boolean doneOrder(Order order) {
		Boolean canProceedToState = canOrderProceedToState(order, OrderState.DONE);
		Boolean result = Boolean.FALSE;
		if (canProceedToState) {
			result = proceedOrderToNextState(order);
			processPayment(order);
			orderRepository.update(order);
		}
		return result;
	}

	@Override
	public Boolean canChange(Order order) {
		boolean canOrderBeChanged = order.getState() == OrderState.NEW;
		return canOrderBeChanged;
	}
	
	private Boolean canOrderProceedToState(Order order, OrderState state) {
		return order.getState().canProceedTo(state);
	}
	
	private Boolean proceedOrderToNextState(Order order) {
		return order.getState().nextState(order);
	}
	
	private Boolean proceedOrderToCancelledState(Order order) {
		return order.getState().cancel(order);
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
		return orderPriceCalculatorService.getFullPrice(order);
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
}
