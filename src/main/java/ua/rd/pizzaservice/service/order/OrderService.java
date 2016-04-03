package ua.rd.pizzaservice.service.order;

import ua.rd.pizzaservice.domain.customer.Customer;
import ua.rd.pizzaservice.domain.discount.Discount;
import ua.rd.pizzaservice.domain.order.Order;

public interface OrderService {

	Order placeNewOrder(Customer customer, Integer... pizzasID);
	Order placeNewOrder(Customer customer, Discount discount, Integer... pizzasID);
	Boolean changeOrder(Order order, Integer... pizzasID);
	Boolean processOrder(Order order);
	Boolean cancelOrder(Order order);
	Boolean doneOrder(Order order);

}