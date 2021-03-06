package ua.rd.pizzaservice.repository.inmem.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import ua.rd.pizzaservice.domain.Pizza;
import ua.rd.pizzaservice.domain.Pizza.PizzaType;
import ua.rd.pizzaservice.repository.PizzaRepository;

public class InMemPizzaRepository implements PizzaRepository {

    List<Pizza> pizzas = new ArrayList<>();

    @PostConstruct
    public void cookPizzas() {
        pizzas.add(new Pizza(1, "Margarita", 60d, PizzaType.MEAT));
        pizzas.add(new Pizza(2, "SeaPizza", 90d, PizzaType.SEA));
        pizzas.add(new Pizza(3, "Ayurveda", 80d, PizzaType.VEGETERIAN));
    }

    @Override
    public Pizza getPizzaByID(Integer id) {
        Pizza result = null;
        for (Pizza pizza : pizzas) {
            if (pizza.getId().equals(id)) {
                result = pizza;
                break;
            }
        }
        return result;
    }

	@Override
	public Pizza create(Pizza pizza) {
		if (pizza != null) {
			pizzas.add(pizza);
		}
		return pizza;
	}

	@Override
	public List<Pizza> getAllPizzas() {
		return new ArrayList<>(pizzas);
	}

	@Override
	public Pizza update(Pizza pizza) {
		Pizza pizzaToUpdate = getPizzaByID(pizza.getId());
		delete(pizzaToUpdate);
		create(pizza);
		return pizza;
	}

	@Override
	public void delete(Pizza pizza) {
		pizzas.remove(pizza);
	}

}
