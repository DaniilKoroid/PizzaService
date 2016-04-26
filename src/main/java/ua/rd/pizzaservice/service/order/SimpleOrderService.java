package ua.rd.pizzaservice.service.order;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;

import ua.rd.pizzaservice.domain.accumulationcard.AccumulationCard;
import ua.rd.pizzaservice.domain.customer.Customer;
import ua.rd.pizzaservice.domain.order.Order;
import ua.rd.pizzaservice.domain.order.OrderState;
import ua.rd.pizzaservice.domain.pizza.Pizza;
import ua.rd.pizzaservice.repository.order.OrderRepository;
import ua.rd.pizzaservice.repository.pizza.PizzaRepository;
import ua.rd.pizzaservice.service.accumulationcard.AccumulationCardService;
import ua.rd.pizzaservice.service.discount.DiscountService;

@Service
public class SimpleOrderService implements OrderService {

	private static final int MIN_PIZZA_IN_ORDER_COUNT = 1;
	private static final int MAX_PIZZA_IN_ORDER_COUNT = 10;

	private DiscountService discountService;
	private AccumulationCardService accCardService;
	private PizzaRepository pizzaRepository;
	private OrderRepository orderRepository;

	SimpleOrderService() {
	}

	@Autowired
	public SimpleOrderService(DiscountService discountService, AccumulationCardService accCardService,
			PizzaRepository pizzaRepository, OrderRepository orderRepository) {
		this.discountService = discountService;
		this.accCardService = accCardService;
		this.pizzaRepository = pizzaRepository;
		this.orderRepository = orderRepository;
	}

	@Override
	public Order placeNewOrder(Customer customer, Integer... pizzasID) {
		checkOrderedPizzasNumber(pizzasID);
		checkCustomerExistance(customer);

		Map<Pizza, Integer> pizzas = pizzasByArrOfId(pizzasID);
		Order newOrder = createOrder(customer, pizzas);

		orderRepository.saveOrder(newOrder);
		return newOrder;
	}

	private Order createOrder(Customer customer, Map<Pizza, Integer> pizzas) {
		Order newOrder = createOrder();
		newOrder.setState(OrderState.NEW);
		newOrder.setCustomer(customer);
		newOrder.setPizzas(pizzas);
		newOrder.setCreationDate(LocalDateTime.now());
		newOrder.setDeliveryDate(LocalDateTime.now().plusMinutes(30));
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
			Pizza pizza = pizzaRepository.getPizzaByID(id);

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

	private void checkCustomerExistance(Customer customer) {
		if (customer == null) {
			throw new IllegalArgumentException("Customer must exist to place new order");
		}
	}

	@Override
	public Boolean changeOrder(Order order, Integer... pizzasID) {
		checkOrderedPizzasNumber(pizzasID);
		Boolean canChange = canChange(order);
		if (canChange) {
			Map<Pizza, Integer> pizzas = pizzasByArrOfId(pizzasID);
			canChange = order.changeOrder(pizzas);
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
		}
		return result;
	}

	@Override
	public Boolean cancelOrder(Order order) {
		return order.cancel();
	}

	@Override
	public Boolean doneOrder(Order order) {
		Boolean canProceedToState = order.canProceedToState(OrderState.DONE);
		Boolean result = Boolean.FALSE;
		if (canProceedToState) {
			result = order.nextState();
			processPayment(order);
		}
		return result;
	}

	private Boolean processPayment(Order order) {
		Customer customer = order.getCustomer();
		if (accCardService.hasAccumulationCard(customer)) {
			Double priceWithDiscounts = discountService.calculatePriceWithDiscounts(order);
			AccumulationCard card = accCardService.getAccumulationCardByCustomer(customer);
			card.use(priceWithDiscounts);
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
}
