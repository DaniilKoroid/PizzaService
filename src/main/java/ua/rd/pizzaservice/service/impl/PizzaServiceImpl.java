package ua.rd.pizzaservice.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.rd.pizzaservice.domain.Pizza;
import ua.rd.pizzaservice.infrastructure.exceptions.NoSuchEntityException;
import ua.rd.pizzaservice.repository.PizzaRepository;
import ua.rd.pizzaservice.service.PizzaService;

@Service
public class PizzaServiceImpl implements PizzaService {

	@Autowired
	PizzaRepository pizzaRep;
	
	@Override
	public Pizza create(Pizza pizza) {
		checkPizzaExistance(pizza);
		return pizzaRep.create(pizza);
	}

	@Override
	public Pizza getPizzaByID(Integer id) {
		Pizza pizza = pizzaRep.getPizzaByID(id);
		checkPizzaExistance(id, pizza);
		return pizza;
	}

	@Override
	public List<Pizza> getAllPizzas() {
		return pizzaRep.getAllPizzas();
	}

	@Override
	public Pizza update(Pizza pizza) {
		checkPizzaExistance(pizza);
		return pizzaRep.update(pizza);
	}

	@Override
	public void delete(Pizza pizza) {
		checkPizzaExistance(pizza);
		pizzaRep.delete(pizza);
	}
	
	private void checkPizzaExistance(Integer id, Pizza pizza) {
		if (pizza == null) {
			throw new NoSuchEntityException("Pizza with given id: " + id + " does not exist.", id.longValue());
		}
	}
	
	private void checkPizzaExistance(Pizza pizza) {
		if (pizza == null) {
			throw new IllegalArgumentException("Can't do operations with inexistant pizza.");
		}
	}

}
