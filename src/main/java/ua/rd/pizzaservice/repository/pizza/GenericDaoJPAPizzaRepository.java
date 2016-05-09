package ua.rd.pizzaservice.repository.pizza;

import java.util.List;

import org.springframework.stereotype.Repository;

import ua.rd.pizzaservice.domain.pizza.Pizza;
import ua.rd.pizzaservice.repository.GenericDaoJPAImpl;

@Repository
public class GenericDaoJPAPizzaRepository extends GenericDaoJPAImpl<Pizza, Integer> implements PizzaRepository {

	@Override
	public Pizza getPizzaByID(Integer id) {
		return em.find(Pizza.class, id); 
	}

	@Override
	public List<Pizza> getAllPizzas() {
		return findAll("findAllPizzas");
	}

}
