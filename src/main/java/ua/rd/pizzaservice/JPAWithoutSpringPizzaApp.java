package ua.rd.pizzaservice;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import ua.rd.pizzaservice.domain.pizza.Pizza;
import ua.rd.pizzaservice.domain.pizza.Pizza.PizzaType;

public class JPAWithoutSpringPizzaApp {

	public static void main(String[] args) {

		String persistenceUnitName = "jpa_mysql";
		EntityManagerFactory emf = null;
		EntityManager em = null;
		
		Pizza pizza = new Pizza();
		pizza.setName("Margarita");
		pizza.setPrice(60.0d);
		pizza.setType(PizzaType.VEGETERIAN);
		
		try {
			emf = Persistence.createEntityManagerFactory(persistenceUnitName);
			em = emf.createEntityManager();
			
			em.getTransaction().begin();
			em.persist(pizza);
			em.getTransaction().commit();
			
			em.find(Pizza.class, 1);
		} finally {
			em.close();
			emf.close();
		}
	}

}
