package ua.rd.pizzaservice.repository.order;

import javax.persistence.Persistence;

import org.springframework.stereotype.Repository;

import ua.rd.pizzaservice.domain.order.Order;
import ua.rd.pizzaservice.repository.GenericDaoJPAImpl;

@Repository
public class GenericDaoJPAOrderRepository extends GenericDaoJPAImpl<Order, Long> implements OrderRepository {
	
	public GenericDaoJPAOrderRepository() {
		emf = Persistence.createEntityManagerFactory("jpa_mysql");
	}
	
	public GenericDaoJPAOrderRepository(String persistenceUnitName) {
		emf = Persistence.createEntityManagerFactory(persistenceUnitName);
	}

	@Override
	public Long saveOrder(Order newOrder) {
		return create(newOrder).getId();
	}

}
