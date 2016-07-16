package ua.rd.pizzaservice.domain.discount;

import ua.rd.pizzaservice.domain.Order;

public interface Discount {

	Double calculateDiscount(Order order);

	Boolean isAppliable(Order order);
}
