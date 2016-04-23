package ua.rd.pizzaservice;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAWithoutSpringPizzaApp {

	public static void main(String[] args) {

		String persistenceUnitName = "jpa_mysql";
		EntityManagerFactory emf = null;
		EntityManager em = null;
		
		try {
			emf = Persistence.createEntityManagerFactory(persistenceUnitName);
			em = emf.createEntityManager();
			// persist operations
		} finally {
			em.close();
			emf.close();
		}
	}

}
