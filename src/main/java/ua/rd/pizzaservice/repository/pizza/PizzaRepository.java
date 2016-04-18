package ua.rd.pizzaservice.repository.pizza;

import ua.rd.pizzaservice.domain.pizza.Pizza;

public interface PizzaRepository {

	Pizza getPizzaByID(Integer id);
}
