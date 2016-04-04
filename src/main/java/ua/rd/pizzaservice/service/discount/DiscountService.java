package ua.rd.pizzaservice.service.discount;

import ua.rd.pizzaservice.domain.order.Order;

public interface DiscountService {

	Double calculateFinalDiscountAmount(Order order);

	Double calculateDiscountsAmount(Order order);

	Double calculatePriceWithDiscounts(Order order);
}
