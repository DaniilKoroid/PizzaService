package ua.rd.pizzaservice.repository.discount;

import java.util.List;

import ua.rd.pizzaservice.domain.discount.Discount;
import ua.rd.pizzaservice.domain.order.Order;

public interface DiscountRepository {

	List<Discount> getAppliableDiscounts(Order order);
}
