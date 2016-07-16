package ua.rd.pizzaservice.repository.inmem.impl;

import java.util.ArrayList;
import java.util.List;

import ua.rd.pizzaservice.domain.Order;
import ua.rd.pizzaservice.repository.OrderRepository;

public class InMemOrderRepository implements OrderRepository {

    private List<Order> orders = new ArrayList<>();

    @Override
    public Long saveOrder(Order newOrder) {
        orders.add(newOrder);
        return newOrder.getId();
    }

	@Override
	public Order create(Order order) {
		if (order != null) {
			orders.add(order);
		}
		return order;
	}

	@Override
	public Order read(Long id) {
		for (Order order : orders) {
			if (order.getId().equals(id)) {
				return order;
			}
		}
		return null;
	}

	@Override
	public Order update(Order newOrder) {
		Order oldOrder = read(newOrder.getId());
		delete(oldOrder);
		create(newOrder);
		return newOrder;
	}

	@Override
	public void delete(Order order) {
		orders.remove(order);
	}

	@Override
	public List<Order> findAllOrders() {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

}
