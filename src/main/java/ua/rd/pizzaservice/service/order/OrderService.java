package ua.rd.pizzaservice.service.order;

import ua.rd.pizzaservice.domain.customer.Customer;
import ua.rd.pizzaservice.domain.discount.Discount;
import ua.rd.pizzaservice.domain.order.Order;

public interface OrderService {

	Order placeNewOrder(Customer customer, Integer... pizzasID);
	Order placeNewOrder(Customer customer, Discount discount, Integer... pizzasID);
	boolean changeOrder(Order order, Integer... pizzasID);
	boolean processOrder(Order order);
	boolean cancelOrder(Order order);
	boolean doneOrder(Order order);

}