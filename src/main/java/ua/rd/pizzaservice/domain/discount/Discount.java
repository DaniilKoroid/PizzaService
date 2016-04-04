package ua.rd.pizzaservice.domain.discount;

import ua.rd.pizzaservice.domain.order.Order;

public interface Discount {

	Double calculateDiscount(Order order);

	Boolean isAppliable(Order order);
}
