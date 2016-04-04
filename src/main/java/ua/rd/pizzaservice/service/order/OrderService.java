package ua.rd.pizzaservice.service.order;

import ua.rd.pizzaservice.domain.customer.Customer;
import ua.rd.pizzaservice.domain.order.Order;

public interface OrderService {

	Order placeNewOrder(Customer customer, Integer... pizzasID);

	Boolean changeOrder(Order order, Integer... pizzasID);

	Boolean canChange(Order order);

	Boolean processOrder(Order order);

	Boolean cancelOrder(Order order);

	Boolean doneOrder(Order order);

	Double getFullPrice(Order order);

	Double getDiscountAmount(Order order);

	Double getFinalPrice(Order order);
}