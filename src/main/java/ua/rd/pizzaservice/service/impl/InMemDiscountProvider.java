package ua.rd.pizzaservice.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;

import ua.rd.pizzaservice.domain.Order;
import ua.rd.pizzaservice.domain.discount.Discount;
import ua.rd.pizzaservice.domain.discount.impl.FourPizzaDiscount;
import ua.rd.pizzaservice.service.DiscountProvider;

@Repository
public class InMemDiscountProvider implements DiscountProvider {

	private List<Discount> discounts = new ArrayList<>();

	@PostConstruct
	public void determineDiscounts() {
	    discounts.add(new FourPizzaDiscount());
	}

	@Override
	public List<Discount> getAppliableDiscounts(Order order) {
		List<Discount> appliableDiscounts = new ArrayList<>();
		for (Discount discount : discounts) {
			if (discount.isAppliable(order)) {
				appliableDiscounts.add(discount);
			}
		}
		if (appliableDiscounts.isEmpty()) {
			appliableDiscounts = java.util.Collections.emptyList();
		}
		return appliableDiscounts;
	}

}
