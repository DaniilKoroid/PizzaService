package ua.rd.pizzaservice.service.order;

import java.util.List;

import ua.rd.pizzaservice.domain.address.Address;
import ua.rd.pizzaservice.domain.customer.Customer;
import ua.rd.pizzaservice.domain.order.Order;

public interface OrderService {

	Order placeNewOrder(Customer customer, Address deliveryAddress, Integer... pizzasID);

	Boolean canChange(Order order);
	
	Boolean changeOrder(Order order, Integer... pizzasID);

	Boolean processOrder(Order order);

	Boolean cancelOrder(Order order);

	Boolean doneOrder(Order order);

	Double getFullPrice(Order order);

	Double getDiscountAmount(Order order);

	Double getFinalPrice(Order order);

	Order create(Order order);

	Order findById(Long id);

	List<Order> findAllOrders();

	Long saveOrder(Order newOrder);
}