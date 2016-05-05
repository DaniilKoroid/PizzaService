package ua.rd.pizzaservice;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ua.rd.pizzaservice.domain.pizza.Pizza;
import ua.rd.pizzaservice.domain.pizza.Pizza.PizzaType;
import ua.rd.pizzaservice.repository.pizza.PizzaRepository;

public class JPAWithSpringApp {

	public static void main(String[] args) {
		ConfigurableApplicationContext repContext = new ClassPathXmlApplicationContext("repositoryMySQLContext.xml");
		repContext.refresh();
		ConfigurableApplicationContext appContext = new ClassPathXmlApplicationContext(
				new String[] { "appContext.xml" }, repContext);
		
		PizzaRepository pizzaRepository = appContext.getBean(PizzaRepository.class);
		
		Pizza newPizza = new Pizza(null, "newPizzaName", 100.0d, PizzaType.MEAT);
		Pizza createdPizza = pizzaRepository.create(newPizza);
		Pizza pizza = pizzaRepository.getPizzaByID(createdPizza.getId());
		System.out.println(pizza);
	}

}
