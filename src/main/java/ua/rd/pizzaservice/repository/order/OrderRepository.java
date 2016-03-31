package ua.rd.pizzaservice.repository.order;

import ua.rd.pizzaservice.domain.order.Order;

public interface OrderRepository {

	Long saveOrder(Order newOrder);
}
