package ua.rd.pizzaservice.service.impl;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;

import ua.rd.pizzaservice.domain.Order;
import ua.rd.pizzaservice.domain.OrderState;
import ua.rd.pizzaservice.domain.Pizza;
import ua.rd.pizzaservice.domain.Pizza.PizzaType;
import ua.rd.pizzaservice.service.OrderPriceCalculatorService;

public class OrderPriceCalculatorServiceImplTest {

	private OrderPriceCalculatorService service;
	private Order order;
	
	@Before
	public void setUpService() {
		service = new OrderPriceCalculatorServiceImpl();
		order = new Order();
	}
	
	@Test
	public void testCalculateFullPriceDontDependsOnOrderStatus() {
		System.out.println("test calculate full price dont depends on order status");
		Map<Pizza, Integer> pizzaMap = getDefaultPizzaMap();
		order.setPizzas(pizzaMap);
		double totalPrice = calculateTotalPrice(pizzaMap);
		double eps = 1E-5;
		OrderState[] orderStateValues = OrderState.values();
		for (OrderState orderState : orderStateValues) {
			order.setState(orderState);
			assertEquals(totalPrice, service.getFullPrice(order), eps);
		}
	}

	@Test
	public void testCalculateFullPrice() {
		System.out.println("test calculate full price");
		Map<Pizza, Integer> pizzaMap = getDefaultPizzaMap();
		order.setPizzas(pizzaMap);
		double expectedTotalPrice = calculateTotalPrice(pizzaMap);
		double actualTotalPrice = service.getFullPrice(order);
		double eps = 1E-5;
		assertEquals(expectedTotalPrice, actualTotalPrice, eps);
	}
	
	private Map<Pizza, Integer> getDefaultPizzaMap() {
		Map<Pizza, Integer> pizzaMap = new HashMap<Pizza, Integer>() {
			static final long serialVersionUID = 920564268758649188L;

			{
				put(new Pizza(1, "Margarita", 60d, PizzaType.MEAT), 1);
				put(new Pizza(2, "SeaPizza", 90d, PizzaType.SEA), 1);
				put(new Pizza(3, "Ayurveda", 80d, PizzaType.VEGETERIAN), 1);
			}
		};
		return pizzaMap;
	}
	
	private Double calculateTotalPrice(Map<Pizza, Integer> pizzas) {
		double result = 0d;
		for (Entry<Pizza, Integer> entry : pizzas.entrySet()) {
			double price = entry.getKey().getPrice();
			Integer count = entry.getValue();
			result += price * count;
		}
		return result;
	}
}
