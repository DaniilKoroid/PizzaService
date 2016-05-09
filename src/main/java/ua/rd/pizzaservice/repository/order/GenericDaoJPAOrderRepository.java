package ua.rd.pizzaservice.repository.order;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import ua.rd.pizzaservice.domain.order.Order;
import ua.rd.pizzaservice.repository.GenericDaoJPAImpl;

@Repository
public class GenericDaoJPAOrderRepository extends GenericDaoJPAImpl<Order, Long> implements OrderRepository {
	
	@Override
	public Long saveOrder(Order newOrder) {
		return create(newOrder).getId();
	}

	@Override
	public List<Order> findAllOrders() {
		return findAll("findAllOrders");
	}

	@Override
	public Order read(Long id) {
		Query namedQuery = em.createNamedQuery("findOrder");
		namedQuery.setParameter("id", id);
		Object result = namedQuery.getSingleResult();
		Order order = (Order) result;
		return order;
	}

}
