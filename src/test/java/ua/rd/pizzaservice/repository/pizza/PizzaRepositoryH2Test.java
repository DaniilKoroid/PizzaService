package ua.rd.pizzaservice.repository.pizza;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ua.rd.pizzaservice.domain.pizza.Pizza;
import ua.rd.pizzaservice.domain.pizza.Pizza.PizzaType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:repositoryH2Context.xml")
@ActiveProfiles("test")
public class PizzaRepositoryH2Test {

	@Autowired
	private PizzaRepository pizzaRepository;

	// @Autowired
	// public void setPizzaRepository(PizzaRepository pizzaRep) {
	// pizzaRepository = pizzaRep;
	// }

	@Test
	public void testGetPizzaByID() {
		Pizza pizza = pizzaRepository.getPizzaByID(1);
		System.out.println(pizza);
	}

	@Test
	public void testCreate() {
		Pizza newPizza = new Pizza(null, "newPizzaName", 100.0d, PizzaType.MEAT);
		Pizza createdPizza = pizzaRepository.create(newPizza);
		assertNotNull(createdPizza.getId());
	}

}
