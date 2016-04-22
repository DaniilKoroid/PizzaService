package ua.rd.pizzaservice.domain.discount;

import java.util.Map;

import ua.rd.pizzaservice.domain.order.Order;
import ua.rd.pizzaservice.domain.pizza.Pizza;

public class FourPizzaDiscount implements Discount {

	protected static final Double DEFAULT_DISCOUNT_AMOUNT_FOR_UNAPPLIABLE = 0d;
	protected static final int PIZZA_MIN_COUNT_FOR_DISCOUNT = 4;
	protected static final double DISCOUNT_PERCENTAGE_FOR_MAX_PRICED_PIZZA = 0.3d;

	@Override
	public Double calculateDiscount(Order order) {
		Map<Pizza, Integer> pizzas = order.getPizzas();
		if (!isAppliable(pizzas)) {
			return DEFAULT_DISCOUNT_AMOUNT_FOR_UNAPPLIABLE;
		}
		Double maxPizzaPrice = getMaxPizzaPrice(pizzas);
		Double discountAmount = calculateDiscountAmount(maxPizzaPrice);
		return discountAmount;

	}

	@Override
	public Boolean isAppliable(Order order) {
		Map<Pizza, Integer> pizzas = order.getPizzas();
		return isAppliable(pizzas);
	}

	private Boolean isAppliable(Map<Pizza, Integer> pizzas) {
		Boolean result = (getOrderSize(pizzas) >= PIZZA_MIN_COUNT_FOR_DISCOUNT);
		return result;
	}

	private int getOrderSize(Map<Pizza, Integer> pizzas) {
		int size = 0;
		for (Integer value : pizzas.values()) {
			size += value;
		}
		return size;
	}

	private Double getMaxPizzaPrice(Map<Pizza, Integer> pizzas) {
		double maxPrice = 0d;
		for (Pizza pizza : pizzas.keySet()) {
			if (pizza.getPrice() > maxPrice) {
				maxPrice = pizza.getPrice();
			}
		}
		return maxPrice;
	}

	private Double calculateDiscountAmount(Double maxPizzaPrice) {
		return DISCOUNT_PERCENTAGE_FOR_MAX_PRICED_PIZZA * maxPizzaPrice;
	}
}
