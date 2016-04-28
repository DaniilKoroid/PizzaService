package ua.rd.pizzaservice.repository.order;

import ua.rd.pizzaservice.domain.order.Order;

public interface OrderRepository {

	Order create(Order order);
	
	Order read(Long id);
	
	Order update(Order newOrder);
	
	void delete(Order order);
	
	Long saveOrder(Order newOrder);
}
