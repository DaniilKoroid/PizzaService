package ua.rd.pizzaservice.repository.pizza;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ua.rd.pizzaservice.domain.pizza.Pizza;

@Repository
public class JPAPizzaRepository implements PizzaRepository {

	@PersistenceContext
	private EntityManager em;
	
	// @Autowired
//	@PersistenceUnit
//	private EntityManagerFactory emf;

	protected JPAPizzaRepository() {
	}
	
	@Override
	@Transactional
	public Pizza create(Pizza pizza) {
		em.persist(pizza);
		return pizza;
	}

	@Override
//	@Transactional(readOnly = true)
	public Pizza getPizzaByID(Integer id) {
		return em.find(Pizza.class, id);
	}

	@Override
	public List<Pizza> getAllPizzas() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pizza update(Pizza pizza) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Pizza pizza) {
		// TODO Auto-generated method stub

	}

}
