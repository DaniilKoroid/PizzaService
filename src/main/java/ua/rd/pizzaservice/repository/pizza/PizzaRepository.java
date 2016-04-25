package ua.rd.pizzaservice.repository.pizza;

import java.util.List;

import ua.rd.pizzaservice.domain.pizza.Pizza;

public interface PizzaRepository {

	void add(Pizza pizza);

	Pizza getPizzaByID(Integer id);

	List<Pizza> getAllPizzas();

	void update(Pizza pizza);

	void delete(Pizza pizza);
}
