package ua.rd.pizzaservice.repository.pizza;

import java.util.ArrayList;
import java.util.List;

import infrastructure.Benchmark;
import infrastructure.PostConstruction;
import ua.rd.pizzaservice.domain.pizza.Pizza;
import ua.rd.pizzaservice.domain.pizza.Pizza.PizzaType;

public class InMemPizzaRepository implements PizzaRepository {

	List<Pizza> pizzas = new ArrayList<>();

	@PostConstruction
	public void cookPizzas() {
		pizzas.add(new Pizza(1, "Margarita", 60d, PizzaType.MEAT));
		pizzas.add(new Pizza(2, "SeaPizza", 90d, PizzaType.SEA));
		pizzas.add(new Pizza(3, "Ayurveda", 80d, PizzaType.VEGETERIAN));
	}
	
	public void init() {
		System.out.println("init method");
	}

	@Benchmark(active = true)
	@Override
	public Pizza getPizzaByID(Integer id) {
		Pizza result = null;
		for (Pizza pizza : pizzas) {
			if (pizza.getId() == id) {
				result = pizza;
				break;
			}
		}
		return result;
	}

}
