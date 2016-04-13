package ua.rd.pizzaservice.repository.order;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import ua.rd.pizzaservice.domain.order.Order;

@Repository
public class InMemOrderRepository implements OrderRepository{

	private List<Order> orders = new ArrayList<>();

	@Override
	public Long saveOrder(Order newOrder) {
		orders.add(newOrder);
		return newOrder.getId();
	}

}
