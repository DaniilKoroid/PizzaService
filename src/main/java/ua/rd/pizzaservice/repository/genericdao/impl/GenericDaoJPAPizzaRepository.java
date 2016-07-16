package ua.rd.pizzaservice.repository.genericdao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import ua.rd.pizzaservice.domain.Pizza;
import ua.rd.pizzaservice.repository.PizzaRepository;

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
