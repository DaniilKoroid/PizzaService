package ua.rd.pizzaservice.service.discount;

import ua.rd.pizzaservice.domain.order.Order;

public interface DiscountService {

	Double calculateDiscountAmount(Order order);
}
