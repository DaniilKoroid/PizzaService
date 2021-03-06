package ua.rd.pizzaservice.service;

import java.util.List;

import ua.rd.pizzaservice.domain.Order;
import ua.rd.pizzaservice.domain.discount.Discount;

public interface DiscountProvider {

	List<Discount> getAppliableDiscounts(Order order);
}
