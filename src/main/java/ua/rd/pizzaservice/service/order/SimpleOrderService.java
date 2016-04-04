package ua.rd.pizzaservice.service.order;

import java.util.ArrayList;
import java.util.List;

import ua.rd.pizzaservice.domain.accumulationcard.AccumulationCard;
import ua.rd.pizzaservice.domain.customer.Customer;
import ua.rd.pizzaservice.domain.discount.Discount;
import ua.rd.pizzaservice.domain.order.Order;
import ua.rd.pizzaservice.domain.order.OrderState;
import ua.rd.pizzaservice.domain.pizza.Pizza;
import ua.rd.pizzaservice.repository.order.InMemOrderRepository;
import ua.rd.pizzaservice.repository.order.OrderRepository;
import ua.rd.pizzaservice.repository.pizza.InMemPizzaRepository;
import ua.rd.pizzaservice.repository.pizza.PizzaRepository;
import ua.rd.pizzaservice.service.discount.DiscountService;
import ua.rd.pizzaservice.service.discount.DiscountServiceImpl;

public class SimpleOrderService implements OrderService {
	
	private static final int MIN_PIZZA_IN_ORDER_COUNT = 1;
	private static final int MAX_PIZZA_IN_ORDER_COUNT = 10;
	
	private DiscountService discountService = new DiscountServiceImpl();
	
	private PizzaRepository pizzaRepository = new InMemPizzaRepository();
	private OrderRepository orderRepository = new InMemOrderRepository();
	
	@Override
	public Order placeNewOrder(Customer customer, Integer... pizzasID) {
		return placeNewOrder(customer, null, pizzasID);
	}
	
	@Override
	public Order placeNewOrder(Customer customer, Discount discount, Integer... pizzasID) {
		checkOrderedPizzasNumber(pizzasID);
		
		List<Pizza> pizzas = pizzasByArrOfId(pizzasID);
		Order newOrder = createOrder(customer, pizzas, discount);
		
		orderRepository.saveOrder(newOrder);
		return newOrder;
	}
	
	private Order createOrder(Customer customer, List<Pizza> pizzas, Discount discount) {
		Order newOrder = new Order(customer, pizzas, discount);
		return newOrder;
	}

	private List<Pizza> pizzasByArrOfId(Integer... pizzasID) {
		List<Pizza> pizzas = new ArrayList<>();

        for(Integer id : pizzasID){
        	pizzas.add(pizzaRepository.getPizzaByID(id));
        }
		return pizzas;
	}
	
	private void checkOrderedPizzasNumber(Integer... pizzasId) {
		if(pizzasId.length < MIN_PIZZA_IN_ORDER_COUNT
				|| pizzasId.length > MAX_PIZZA_IN_ORDER_COUNT) {
			
			throw new IllegalArgumentException("Can't place order with "
					+ "not allowed number of pizzas.");
		}
	}

	@Override
	public Boolean changeOrder(Order order, Integer... pizzasID) {
		Boolean canChange = order.canChange();
		if(canChange) {
			List<Pizza> pizzas = pizzasByArrOfId(pizzasID);
			canChange = order.changeOrder(pizzas);
		}
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
		if(customer.isAccumulationCardPresent()) {
			Double priceWithDiscounts = discountService.calculatePriceWithDiscounts(order);
			AccumulationCard card = customer.getAccumulationCard();
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
