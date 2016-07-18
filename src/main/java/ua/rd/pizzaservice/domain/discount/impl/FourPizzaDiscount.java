package ua.rd.pizzaservice.domain.discount.impl;

import java.util.Map;

import ua.rd.pizzaservice.domain.Order;
import ua.rd.pizzaservice.domain.Pizza;
import ua.rd.pizzaservice.domain.discount.Discount;

public class FourPizzaDiscount implements Discount {

	protected static final Double DEFAULT_DISCOUNT_AMOUNT_FOR_UNAPPLIABLE = 0d;
	protected static final int PIZZA_MIN_COUNT_FOR_DISCOUNT = 4;
	protected static final double DISCOUNT_PERCENTAGE_FOR_MAX_PRICED_PIZZA = 0.3d;

	@Override
	public Double calculateDiscount(Order order) {
		checkOrderExistance(order);
		Double discountAmount = DEFAULT_DISCOUNT_AMOUNT_FOR_UNAPPLIABLE;
		
		Map<Pizza, Integer> pizzas = order.getPizzas();
		if (checkPizzasExistance(pizzas)) {
			if (isAppliable(pizzas)) {
				Double maxPizzaPrice = getMaxPizzaPrice(pizzas);
				discountAmount = calculateDiscountAmount(maxPizzaPrice);
			}
		}
		return discountAmount;

	}

	@Override
	public Boolean isAppliable(Order order) {
		checkOrderExistance(order);
		Map<Pizza, Integer> pizzas = order.getPizzas();
		return isAppliable(pizzas);
	}

	private Boolean isAppliable(Map<Pizza, Integer> pizzas) {
		Boolean result = (getOrderSize(pizzas) >= PIZZA_MIN_COUNT_FOR_DISCOUNT);
		return result;
	}

	private int getOrderSize(Map<Pizza, Integer> pizzas) {
		int size = 0;
		if (checkPizzasExistance(pizzas)) {
			for (Integer value : pizzas.values()) {
				size += value;
			}
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

	private void checkOrderExistance(Order order) {
		if (order == null) {
			throw new IllegalArgumentException("Can't discount unexistant order!");
		}
	}

	private boolean checkPizzasExistance(Map<Pizza, Integer> pizzas) {
		return pizzas != null;
	}
}
