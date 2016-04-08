package ua.rd.pizzaservice.service;

import ua.rd.pizzaservice.domain.pizza.Pizza;
import ua.rd.pizzaservice.domain.pizza.Pizza.PizzaType;

public class PizzaService {

	public Pizza createPizza(Integer id, String name, Double price, PizzaType type) {
		return new Pizza(id, name, price, type);
	}
}
