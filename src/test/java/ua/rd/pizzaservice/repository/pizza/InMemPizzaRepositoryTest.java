package ua.rd.pizzaservice.repository.pizza;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import ua.rd.pizzaservice.domain.pizza.Pizza;
import ua.rd.pizzaservice.domain.pizza.Pizza.PizzaType;

public class InMemPizzaRepositoryTest {

    PizzaRepository pizzaRepository;

    @Before
    public void setUpVariables() {
        pizzaRepository = new InMemPizzaRepository();
        ((InMemPizzaRepository) pizzaRepository).cookPizzas();
    }

    @Test
    public void testGetPizzaByIdReturnsAppropriatePizza() {
        System.out.println("test get pizza by id returns appropriate pizza");
        int id = 1;
        Pizza pizzaOne = new Pizza(1, "Margarita", 60d, PizzaType.MEAT);
        Pizza pizzaByID = pizzaRepository.getPizzaByID(id);
        assertEquals(pizzaOne, pizzaByID);
    }

    @Test
    public void testGetPizzaByIdReturnsNullIfThereIsNoSuchPizza() {
        System.out.println(
                "test get pizza by id returns null if there is no such pizza");
        int unexistingId = 123456;
        Pizza pizzaBydId = pizzaRepository.getPizzaByID(unexistingId);
        assertEquals(null, pizzaBydId);
    }

}
