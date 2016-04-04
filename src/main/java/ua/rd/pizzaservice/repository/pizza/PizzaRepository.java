package ua.rd.pizzaservice.repository.pizza;

import infrastructure.Benchmark;
import ua.rd.pizzaservice.domain.pizza.Pizza;

public interface PizzaRepository {

	@Benchmark	
	Pizza getPizzaByID(Integer id);
}
