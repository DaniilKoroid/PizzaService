package ua.rd.pizzaservice;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import ua.rd.pizzaservice.domain.pizza.Pizza;
import ua.rd.pizzaservice.domain.pizza.Pizza.PizzaType;

public class JPAWithoutSpringApp {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence
                .createEntityManagerFactory("jpa_mysql");
        EntityManager em = emf.createEntityManager();
        Pizza pizza = new Pizza();
//        pizza.setId(3);
        pizza.setName("Seazza");
        pizza.setPrice(111.0d);
        pizza.setType(PizzaType.SEA);

        try {
            em.getTransaction().begin();
            em.persist(pizza);
            em.getTransaction().commit();

            Pizza find = em.find(Pizza.class, 2);
            System.out.println(find);
        } finally {
            em.close();
            emf.close();
        }

    }

}
