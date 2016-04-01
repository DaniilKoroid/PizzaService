package ua.rd.pizzaservice.service.order;

import ua.rd.pizzaservice.domain.customer.Customer;
import ua.rd.pizzaservice.domain.order.Order;

public interface OrderService {

	Order placeNewOrder(Customer customer, Integer... pizzasID);

}