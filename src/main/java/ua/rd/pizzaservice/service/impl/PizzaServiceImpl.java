package ua.rd.pizzaservice.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.rd.pizzaservice.domain.Pizza;
import ua.rd.pizzaservice.repository.PizzaRepository;
import ua.rd.pizzaservice.service.PizzaService;

@Service
public class PizzaServiceImpl implements PizzaService {

	@Autowired
	PizzaRepository pizzaRep;
	
	@Override
	public Pizza create(Pizza pizza) {
		return pizzaRep.create(pizza);
	}

	@Override
	public Pizza getPizzaByID(Integer id) {
		Pizza pizza = pizzaRep.getPizzaByID(id);
		if (pizza == null) {
			throw new RuntimeException("No pizza with given id: " + id + ".");
		}
		return pizza;
	}

	@Override
	public List<Pizza> getAllPizzas() {
		return pizzaRep.getAllPizzas();
	}

	@Override
	public Pizza update(Pizza pizza) {
		return pizzaRep.update(pizza);
	}

	@Override
	public void delete(Pizza pizza) {
		pizzaRep.delete(pizza);
	}

}
