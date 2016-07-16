package ua.rd.pizzaservice.repository;

import java.util.List;

import ua.rd.pizzaservice.domain.Pizza;

public interface PizzaRepository {

	Pizza create(Pizza pizza);

	Pizza getPizzaByID(Integer id);

	List<Pizza> getAllPizzas();

	Pizza update(Pizza pizza);

	void delete(Pizza pizza);
}
