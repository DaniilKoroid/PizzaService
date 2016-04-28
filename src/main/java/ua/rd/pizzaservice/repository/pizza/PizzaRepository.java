package ua.rd.pizzaservice.repository.pizza;

import java.util.List;

import ua.rd.pizzaservice.domain.pizza.Pizza;

public interface PizzaRepository {

	Pizza create(Pizza pizza);

	Pizza getPizzaByID(Integer id);

	List<Pizza> getAllPizzas();

	Pizza update(Pizza pizza);

	void delete(Pizza pizza);
}
