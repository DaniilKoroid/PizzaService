package ua.rd.pizzaservice.service.order;

import java.util.List;
import java.util.Map;

import ua.rd.pizzaservice.domain.Address;
import ua.rd.pizzaservice.domain.Customer;
import ua.rd.pizzaservice.domain.Order;
import ua.rd.pizzaservice.domain.Pizza;

public interface OrderService {

	Order placeNewOrder(Customer customer, Address deliveryAddress, Integer... pizzasID);
	
	Order placeNewOrder(Customer customer, Address deliveryAddress, Map<Pizza, Integer> pizzas);

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