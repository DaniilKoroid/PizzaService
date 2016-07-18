package ua.rd.pizzaservice.service.impl;

import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;

import ua.rd.pizzaservice.domain.Order;
import ua.rd.pizzaservice.domain.Pizza;
import ua.rd.pizzaservice.service.OrderPriceCalculatorService;

@Service
public class OrderPriceCalculatorServiceImpl implements OrderPriceCalculatorService {

	@Override
	public Double getFullPrice(Order order) {
		checkOrder(order);
		Map<Pizza, Integer> pizzas = order.getPizzas();
		boolean pizzasExist = checkPizzas(pizzas);
		double totalPrice = 0d;
		if (pizzasExist) {
			for (Entry<Pizza, Integer> entrySet : pizzas.entrySet()) {
				double pricePerAll = 0d;
				double pricePerOne = entrySet.getKey().getPrice();
				Integer pizzaCount = entrySet.getValue();
				pricePerAll = pricePerOne * pizzaCount;
				totalPrice += pricePerAll;
			}
		}
		return totalPrice;
	}

	private void checkOrder(Order order) {
		if (order == null) {
			throw new IllegalArgumentException("Order given to calculate it's price is not existing!");
		}
	}

	private boolean checkPizzas(Map<Pizza, Integer> pizzas) {
		return pizzas != null;
	}

}
