package ua.rd.pizzaservice.service;

import ua.rd.pizzaservice.domain.Order;

public interface OrderPriceCalculatorService {

	Double getFullPrice(Order order);
}
