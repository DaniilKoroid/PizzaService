package ua.rd.pizzaservice.domain.discount;

import java.util.List;

import ua.rd.pizzaservice.domain.order.Order;
import ua.rd.pizzaservice.domain.pizza.Pizza;

public class FourPizzaDiscount implements Discount {

	protected final static int PIZZA_MIN_COUNT_FOR_DISCOUNT = 4;
	
	protected final static double DISCOUNT_PERCENTAGE_FOR_MAX_PRICED_PIZZA = 0.3d;
	
	@Override
	public Double calculateDiscount(Order order) {
		List<Pizza> pizzas = order.getPizzas();
		if(pizzas.size() < PIZZA_MIN_COUNT_FOR_DISCOUNT) {
			return 0d;
		}
		Double maxPizzaPrice = getMaxPizzaPrice(pizzas);
		return DISCOUNT_PERCENTAGE_FOR_MAX_PRICED_PIZZA * maxPizzaPrice;
		
	}
	
	private Double getMaxPizzaPrice(List<Pizza> pizzas) {
		double maxPrice = 0d;
		for (Pizza pizza : pizzas) {
			if (pizza.getPrice() > maxPrice) {
				maxPrice = pizza.getPrice();
			}
		}
		return maxPrice;
	}

}
