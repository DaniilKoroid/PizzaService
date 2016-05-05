package ua.rd.pizzaservice.repository.pizza;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ua.rd.pizzaservice.domain.pizza.Pizza;
import ua.rd.pizzaservice.repository.GenericDaoJPAImpl;

@Repository
@Transactional
public class GenericDaoJPAPizzaRepository extends GenericDaoJPAImpl<Pizza, Integer> implements PizzaRepository {

	@Override
	public Pizza getPizzaByID(Integer id) {
		return em.find(Pizza.class, id); 
	}

	@Override
	public List<Pizza> getAllPizzas() {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

}
