package ua.rd.pizzaservice.service.pizza;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.rd.pizzaservice.domain.pizza.Pizza;
import ua.rd.pizzaservice.repository.pizza.PizzaRepository;

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
		return pizzaRep.getPizzaByID(id);
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
