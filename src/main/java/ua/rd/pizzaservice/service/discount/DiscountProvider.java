package ua.rd.pizzaservice.service.discount;

import java.util.List;

import ua.rd.pizzaservice.domain.discount.Discount;
import ua.rd.pizzaservice.domain.order.Order;

public interface DiscountProvider {

	List<Discount> getAppliableDiscounts(Order order);
}
