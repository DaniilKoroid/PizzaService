package ua.rd.pizzaservice.repository.order;

import org.springframework.stereotype.Repository;

import ua.rd.pizzaservice.domain.order.Order;
import ua.rd.pizzaservice.repository.GenericDaoJPAImpl;

@Repository
public class GenericDaoJPAOrderRepository extends GenericDaoJPAImpl<Order, Long> implements OrderRepository {

	@Override
	public Long saveOrder(Order newOrder) {
		return create(newOrder).getId();
	}

}
