package ua.rd.pizzaservice.service.pizza;

import java.util.List;

import ua.rd.pizzaservice.domain.Pizza;

public interface PizzaService {

	Pizza create(Pizza pizza);

	Pizza getPizzaByID(Integer id);

	List<Pizza> getAllPizzas();

	Pizza update(Pizza pizza);

	void delete(Pizza pizza);
}
