package ua.rd.pizzaservice.service;

import ua.rd.pizzaservice.domain.Order;

public interface DiscountService {

//	Double calculateFinalDiscountAmount(Order order);

	Double calculateDiscountsAmount(Order order);

	Double calculatePriceWithDiscounts(Order order);
}
