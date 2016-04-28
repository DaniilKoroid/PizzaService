package ua.rd.pizzaservice.repository.pizza;

import java.util.List;

import javax.persistence.Persistence;

import org.springframework.stereotype.Repository;

import ua.rd.pizzaservice.domain.pizza.Pizza;
import ua.rd.pizzaservice.repository.GenericDaoJPAImpl;

@Repository
public class GenericDaoJPAPizzaRepository extends GenericDaoJPAImpl<Pizza, Integer> implements PizzaRepository {

	public GenericDaoJPAPizzaRepository() {
		emf = Persistence.createEntityManagerFactory("jpa_mysql");
	}
	
	public GenericDaoJPAPizzaRepository(String persistenceUnitName) {
		emf = Persistence.createEntityManagerFactory(persistenceUnitName);
	}

	@Override
	public Pizza getPizzaByID(Integer id) {
		return read(id);
	}

	@Override
	public List<Pizza> getAllPizzas() {
		throw new UnsupportedOperationException("Not implemented yet.");
	}
}
