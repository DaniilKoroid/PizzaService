package ua.rd.pizzaservice.service.discount;

import java.util.List;

import ua.rd.pizzaservice.domain.discount.Discount;
import ua.rd.pizzaservice.domain.order.Order;
import ua.rd.pizzaservice.repository.discount.DiscountRepository;
import ua.rd.pizzaservice.repository.discount.InMemDiscountRepository;

public class DiscountServiceImpl implements DiscountService {

	private DiscountRepository discountRepository = new InMemDiscountRepository();
	
	@Override
	public Double calculateDiscountAmount(Order order) {
		List<Discount> appliableDiscounts = discountRepository.getAppliableDiscounts(order);
		Double discountAmount = calculateDiscountAmount(order, appliableDiscounts);
		return discountAmount;
	}
	
	private Double calculateDiscountAmount(Order order, List<Discount> discounts) {
		Double discountAmount = 0d;
		for (Discount discount : discounts) {
			discountAmount += discount.calculateDiscount(order);
		}
		return discountAmount;
	}

}
